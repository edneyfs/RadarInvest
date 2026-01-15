package br.com.edneysiqueira.radarinvest.infrastructure.provider;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import br.com.edneysiqueira.radarinvest.domain.provider.ProventosProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "radar.provider.active", havingValue = "mock", matchIfMissing = true)
public class MockProventosProvider implements ProventosProvider {

    @Override
    public java.util.List<EventoAtivo> buscarProventos(String ticker, String tipoAtivo) {
        // Dados simulados para atender ao requisito V1.0 de entrega garantida
        // Em um cenário real, aqui entraria o scraping ou chamada de API

        LocalDate hoje = LocalDate.now();

        if ("HGLG11".equalsIgnoreCase(ticker)) {
            return java.util.List.of(EventoAtivo.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo(tipoAtivo)
                    .tipoEvento("RENDIMENTO")
                    .dataCom(hoje.plusDays(5)) // Data COM futura
                    .dataEx(hoje.plusDays(6))
                    .dataPagamento(hoje.plusDays(15))
                    .valor(new BigDecimal("1.10"))
                    .moeda("BRL")
                    .fonte("Simulado V1.0")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        } else if ("ITSA4".equalsIgnoreCase(ticker)) {
            return java.util.List.of(EventoAtivo.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo(tipoAtivo)
                    .tipoEvento("JCP")
                    .dataCom(hoje.minusDays(2)) // Já passou
                    .dataEx(hoje.minusDays(1))
                    .dataPagamento(hoje.plusDays(20))
                    .valor(new BigDecimal("0.0235"))
                    .moeda("BRL")
                    .fonte("Simulado V1.0")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        } else if ("XPML11".equalsIgnoreCase(ticker)) {
            return java.util.List.of(EventoAtivo.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo(tipoAtivo)
                    .tipoEvento("RENDIMENTO")
                    .dataCom(hoje.withDayOfMonth(25))
                    .dataEx(hoje.withDayOfMonth(26))
                    .dataPagamento(hoje.plusMonths(1).withDayOfMonth(10))
                    .valor(new BigDecimal("0.90"))
                    .moeda("BRL")
                    .fonte("Simulado V1.0")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        } else if ("BBAS3".equalsIgnoreCase(ticker)) {
            return java.util.List.of(EventoAtivo.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo("B3_ACAO") // Garantir tipo correto
                    .tipoEvento("JCP")
                    .dataCom(hoje.plusDays(10))
                    .dataEx(hoje.plusDays(11))
                    .dataPagamento(hoje.plusDays(25))
                    .valor(new BigDecimal("2.45")) // Valor de provento simulado
                    .moeda("BRL")
                    .fonte("Simulado V1.0")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        }

        return java.util.List.of();
    }
}
