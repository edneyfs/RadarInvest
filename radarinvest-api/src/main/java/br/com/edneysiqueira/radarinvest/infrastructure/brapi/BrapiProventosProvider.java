package br.com.edneysiqueira.radarinvest.infrastructure.brapi;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import br.com.edneysiqueira.radarinvest.domain.provider.ProventosProvider;
import br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto.BrapiCashDividend;
// unused import removed
import br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto.BrapiResponse;
import br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto.BrapiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "radar.provider.active", havingValue = "brapi")
@RequiredArgsConstructor
@Slf4j
public class BrapiProventosProvider implements ProventosProvider {

    @Value("${radar.provider.brapi.url}")
    private String apiUrl;

    @Value("${radar.provider.brapi.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<EventoAtivo> buscarProventos(String ticker, String tipoAtivo) {
        try {
            // URL: https://brapi.dev/api/quote/{ticker}?token={token}&dividends=true
            String url = String.format("%s/quote/%s?token=%s&dividends=true", apiUrl, ticker, apiToken);
            log.info("Chamando Brapi API para {}", ticker);

            BrapiResponse response = restTemplate.getForObject(url, BrapiResponse.class);

            if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                BrapiResult result = response.getResults().get(0);

                if (result.getDividendsData() != null && result.getDividendsData().getCashDividends() != null) {
                    List<BrapiCashDividend> dividends = result.getDividendsData().getCashDividends();
                    LocalDate hoje = LocalDate.now();

                    // Estrategia: Retornar todos os proventos com DataCom >= Hoje - 1 dia (para
                    // pegar recentes)
                    // ou se não houver futuros, o mais recente do passado.

                    List<EventoAtivo> futuros = dividends.stream()
                            .filter(d -> d.getLastDatePrior() != null)
                            .filter(d -> parseDate(d.getLastDatePrior()) != null
                                    && parseDate(d.getLastDatePrior()).isAfter(hoje.minusDays(30))) // Ultimos 30 dias
                                                                                                    // para pegar
                                                                                                    // recentes tambem
                            .sorted(Comparator.comparing(d -> parseDate(d.getLastDatePrior()),
                                    Comparator.reverseOrder()))
                            .map(d -> mapToEntity(d, ticker, tipoAtivo))
                            .toList();

                    if (!futuros.isEmpty()) {
                        return futuros;
                    }

                    // Se nao achar nada recente/futuro, tenta pegar o ultimo absoluto
                    return dividends.stream()
                            .filter(d -> d.getLastDatePrior() != null)
                            .max(Comparator.comparing(d -> parseDate(d.getLastDatePrior())))
                            .map(d -> List.of(mapToEntity(d, ticker, tipoAtivo)))
                            .orElse(List.of());
                }
            }

        } catch (Exception e) {
            log.error("Erro ao consultar Brapi para {}: {}", ticker, e.getMessage());
        }

        return List.of();
    }

    private EventoAtivo mapToEntity(BrapiCashDividend dto, String ticker, String tipoAtivo) {
        return EventoAtivo.builder()
                .id(UUID.randomUUID())
                .identificadorAtivo(ticker)
                .tipoAtivo(tipoAtivo)
                .tipoEvento(dto.getLabel())
                .dataCom(parseDate(dto.getLastDatePrior()))
                .dataEx(null) // Brapi as vezes não manda explícito, assumimos dia seguinte útil ou deixamos
                              // null
                .dataPagamento(parseDate(dto.getPaymentDate()))
                .valor(dto.getRate())
                .moeda("BRL")
                .fonte("Brapi.dev")
                .coletadoEm(LocalDateTime.now())
                .build();
    }

    // Brapi data format example: "2026-03-20T00:00:00.000Z" (ISO-like)
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null)
            return null;
        try {
            return LocalDate.parse(dateStr.substring(0, 10)); // Pega so yyyy-MM-dd
        } catch (Exception e) {
            return null;
        }
    }
}
