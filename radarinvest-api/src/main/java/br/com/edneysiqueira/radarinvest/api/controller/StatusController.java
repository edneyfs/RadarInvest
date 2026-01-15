package br.com.edneysiqueira.radarinvest.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Status", description = "Verificação de saúde da aplicação")
public class StatusController {

    @Value("${info.app.version}")
    private String version;

    @Operation(summary = "Verificar status da API", description = "Retorna informações básicas sobre o estado e versão da aplicação.")
    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Map.of("status", "UP", "version", version);
    }
}
