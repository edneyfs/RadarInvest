package br.com.edneysiqueira.radarinvest.infrastructure.service;

import br.com.edneysiqueira.radarinvest.infrastructure.annotation.LogSensitive;
import br.com.edneysiqueira.radarinvest.infrastructure.annotation.SensitiveType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LogMaskingService {

    private final Map<String, SensitiveType> sensitiveFields = new HashMap<>();

    @PostConstruct
    public void init() {
        scanForSensitiveFields();
    }

    private void scanForSensitiveFields() {
        log.info("Iniciando escaneamento de campos sensíveis para mascaramento de logs...");

        // Escaneia classes no pacote DTO (ou lógica de correspondência mais profunda)
        // Como o escaneamento padrão é complexo sem uma configuração refinada,
        // assumimos a busca em pacotes conhecidos ou usamos utilitário de reflexão.
        // Para simplicidade e robustez dentro do Contexto Spring, usamos
        // ClassPathScanningCandidateComponentProvider

        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        // Aceitamos todas as classes para verificar seus campos, mas é melhor focar no pacote DTO
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        String basePackage = "br.com.edneysiqueira.radarinvest.api.dto"; // DTOs Alvo

        Set<BeanDefinition> components = scanner.findCandidateComponents(basePackage);

        for (BeanDefinition bd : components) {
            try {
                Class<?> clazz = ClassUtils.forName(bd.getBeanClassName(), ClassUtils.getDefaultClassLoader());
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(LogSensitive.class)) {
                        LogSensitive annotation = field.getAnnotation(LogSensitive.class);
                        sensitiveFields.put(field.getName(), annotation.strategy());
                        log.debug("Campo sensível encontrado: {}.{} -> estratégia: {}",
                                clazz.getSimpleName(), field.getName(), annotation.strategy());
                    }
                }
            } catch (Exception e) {
                log.warn("Erro ao processar classe para mascaramento: {}", bd.getBeanClassName(), e);
            }
        }
    }

    public String maskLog(String jsonBody) {
        if (jsonBody == null || jsonBody.isEmpty() || sensitiveFields.isEmpty()) {
            return jsonBody;
        }

        // Regex simples para encontrar chaves: "chave" : "valor"
        // Limitação: Não lida perfeitamente com objetos aninhados ou arrays com regex simples,
        // mas é robusto o suficiente para logging de DTOs achatados.

        // Constrói um regex que corresponde a qualquer uma das chaves sensíveis conhecidas
        // Padrão: "(senha|cpf|email)"\s*:\s*"([^"]*)"

        StringBuilder keysRegex = new StringBuilder();
        for (String key : sensitiveFields.keySet()) {
            if (keysRegex.length() > 0) {
                keysRegex.append("|");
            }
            keysRegex.append(Pattern.quote(key));
        }

        String patternString = "\"(" + keysRegex.toString() + ")\"\\s*:\\s*\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(jsonBody);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            SensitiveType strategy = sensitiveFields.get(key);

            String maskedValue = applyMask(value, strategy);

            // A substituição precisa de escape para caracteres especiais literais, especificamente barra invertida
            // e cifrão. Mas valores JSON estritos geralmente não têm isso sem escape.
            // Reconstruímos a parte JSON: "chave":"valorMascarado"

            matcher.appendReplacement(sb, Matcher.quoteReplacement("\"" + key + "\":\"" + maskedValue + "\""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String applyMask(String value, SensitiveType strategy) {
        if (value == null)
            return "null";
        if (value.length() <= 2)
            return "***";

        switch (strategy) {
            case MASK_ALL_BUT_FIRST_3:
                if (value.length() <= 3)
                    return value + "***";
                return value.substring(0, 3) + "***";

            case MASK_ALL_BUT_LAST_3:
                if (value.length() <= 3)
                    return "***" + value;
                return "***" + value.substring(value.length() - 3);

            case MASK_CPF:
                // Esperado 11 dígitos ou formatado 14 caracteres
                // Máscara genérica simples para o "meio"
                if (value.length() < 5)
                    return "***";
                return value.substring(0, 3) + ".***.***-" + value.substring(value.length() - 2);

            case HIDE:
            default:
                return "***";
        }
    }
}
