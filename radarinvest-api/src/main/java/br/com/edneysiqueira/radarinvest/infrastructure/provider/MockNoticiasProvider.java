package br.com.edneysiqueira.radarinvest.infrastructure.provider;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoNoticia;
import br.com.edneysiqueira.radarinvest.domain.provider.NoticiasProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MockNoticiasProvider implements NoticiasProvider {

    @Override
    public List<EventoNoticia> buscarNoticias(String ticker) {
        List<EventoNoticia> noticias = new ArrayList<>();

        // Simulação V1: Notícias "Hardcoded" mas realistas para os Seed Tickers
        if ("HGLG11".equalsIgnoreCase(ticker)) {
            noticias.add(EventoNoticia.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo("B3_FII")
                    .titulo("HGLG11 anuncia nova emissão de cotas")
                    .resumo("Fundo aprovou a 10ª emissão de cotas com montante inicial de R$ 500 milhões.")
                    .url("https://fnet.bmfbovespa.com.br/fnet/publico/exibirDocumento?id=123")
                    .publicadoEm(LocalDateTime.now().minusDays(2))
                    .categoria("EMISSAO")
                    .severidade("ALTO")
                    .fonte("Simulado CVM")
                    .hashDeduplicacao("HGLG11-EMISSAO-10")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        } else if ("ITSA4".equalsIgnoreCase(ticker)) {
            noticias.add(EventoNoticia.builder()
                    .id(UUID.randomUUID())
                    .identificadorAtivo(ticker)
                    .tipoAtivo("B3_ACAO")
                    .titulo("Itaúsa divulga resultados do 1T24")
                    .resumo("Lucro líquido recorrente de R$ 3,5 bilhões, alta de 20% na comparação anual.")
                    .url("https://ri.itausa.com.br")
                    .publicadoEm(LocalDateTime.now().minusDays(5))
                    .categoria("RESULTADO")
                    .severidade("MEDIO")
                    .fonte("Simulado RI")
                    .hashDeduplicacao("ITSA4-RESULTADO-1T24")
                    .coletadoEm(LocalDateTime.now())
                    .build());
        }

        return noticias;
    }
}
