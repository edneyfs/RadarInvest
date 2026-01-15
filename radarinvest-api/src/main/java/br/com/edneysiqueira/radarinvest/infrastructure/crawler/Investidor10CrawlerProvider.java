package br.com.edneysiqueira.radarinvest.infrastructure.crawler;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import br.com.edneysiqueira.radarinvest.domain.provider.ProventosProvider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import br.com.edneysiqueira.radarinvest.domain.entity.DetalhesAtivo;
import br.com.edneysiqueira.radarinvest.domain.provider.InformacoesAtivoProvider;

@Service
@ConditionalOnProperty(name = "radar.provider.active", havingValue = "crawler")
@Slf4j
public class Investidor10CrawlerProvider implements ProventosProvider, InformacoesAtivoProvider {

    private static final String BASE_URL_HOST = "https://investidor10.com.br/";

    @Override
    public java.util.List<EventoAtivo> buscarProventos(String ticker, String tipoAtivo) {
        java.util.List<EventoAtivo> eventos = new java.util.ArrayList<>();
        try {
            String segment = "acoes";
            if ("B3_FII".equalsIgnoreCase(tipoAtivo)) {
                segment = "fiis";
            } else if ("B3_BDR".equalsIgnoreCase(tipoAtivo)) {
                segment = "bdrs";
            }

            String url = BASE_URL_HOST + segment + "/" + ticker.toLowerCase() + "/";
            log.info("Crawling Investidor10 para {}: {}", ticker, url);

            Document doc = Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();

            Element table = doc.selectFirst("#table-dividends-history");

            if (table != null) {
                Elements rows = table.select("tbody tr");
                LocalDate hoje = LocalDate.now();

                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 4) {
                        String tipo = cols.get(0).text().trim();

                        // Ignorar não-financeiros
                        if (tipo.equalsIgnoreCase("Bonificação") || tipo.equalsIgnoreCase("Desdobramento")
                                || tipo.equalsIgnoreCase("Grupamento")) {
                            continue;
                        }

                        String dataComStr = cols.get(1).text();
                        String dataPagStr = cols.get(2).text();
                        String valorStr = cols.get(3).text();

                        LocalDate dataCom = parseDate(dataComStr);
                        LocalDate dataPag = parseDate(dataPagStr);

                        if (dataCom != null) {
                            // Regra:
                            // 1. Data Com Futura/Hoje (Oportunidade)
                            // 2. Data Pagamento Futura/Hoje (Provisionado)
                            // Se qualquer um dos dois for verdade, adiciona a lista.

                            boolean isFuturo = (dataCom.isAfter(hoje) || dataCom.equals(hoje));
                            if (!isFuturo && dataPag != null) {
                                isFuturo = (dataPag.isAfter(hoje) || dataPag.equals(hoje));
                            }

                            if (isFuturo) {
                                eventos.add(createEvento(ticker, tipoAtivo, tipo, dataCom, dataPag, valorStr));
                            }
                        }
                    }
                }
            } else {
                log.warn("Tabela de dividendos não encontrada para {}", ticker);
            }

        } catch (Exception e) {
            log.error("Erro no crawler para {}: {}", ticker, e.getMessage());
        }
        return eventos;
    }

    private EventoAtivo createEvento(String ticker, String tipoAtivo, String tipo, LocalDate dataCom, LocalDate dataPag,
            String valorStr) {
        return EventoAtivo.builder()
                .id(UUID.randomUUID())
                .identificadorAtivo(ticker)
                .tipoAtivo(tipoAtivo)
                .tipoEvento(tipo)
                .dataCom(dataCom)
                .dataEx(dataCom.plusDays(1))
                .dataPagamento(dataPag)
                .valor(parseValor(valorStr))
                .moeda("BRL")
                .fonte("Investidor10 Crawler")
                .coletadoEm(LocalDateTime.now())
                .build();
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseValor(String valorStr) {
        try {
            if (valorStr == null || valorStr.trim().isEmpty() || valorStr.trim().equals("-")) {
                return BigDecimal.ZERO;
            }
            // Remove R$, %, pontos, espaços
            String clean = valorStr.replaceAll("[^0-9,MBmb]", "").trim();

            BigDecimal multiplier = BigDecimal.ONE;
            if (clean.toUpperCase().endsWith("M")) {
                multiplier = new BigDecimal("1000000");
                clean = clean.substring(0, clean.length() - 1);
            } else if (clean.toUpperCase().endsWith("B")) {
                multiplier = new BigDecimal("1000000000");
                clean = clean.substring(0, clean.length() - 1);
            }

            clean = clean.replace(".", "").replace(",", ".");
            return new BigDecimal(clean).multiply(multiplier);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public DetalhesAtivo buscarDetalhes(String ticker, String tipoAtivo) {
        DetalhesAtivo detalhes = new DetalhesAtivo();
        detalhes.setTicker(ticker);

        try {
            String segment = "acoes";
            if ("B3_FII".equalsIgnoreCase(tipoAtivo)) {
                segment = "fiis";
            } else if ("B3_BDR".equalsIgnoreCase(tipoAtivo)) {
                segment = "bdrs";
            }

            String url = BASE_URL_HOST + segment + "/" + ticker.toLowerCase() + "/";
            log.info("Crawling Detalhes Investidor10 para {}: {}", ticker, url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(10000)
                    .get();

            // Scrape Header Name
            Element nameEl = doc.selectFirst("h2.name-company");
            if (nameEl != null)
                detalhes.setNome(nameEl.text());

            // Scrape Cards (Cotação, DY, P/VP, etc)
            Elements cards = doc.select("div._card");
            for (Element card : cards) {
                String title = card.select("div._card-header span").text().toUpperCase();
                String valueStr = card.select("div._card-body span").first() != null
                        ? card.select("div._card-body span").first().text()
                        : "";

                if (title.contains("COTAÇÃO")) {
                    detalhes.setCotacao(parseValor(valueStr));
                } else if (title.contains("DY") || title.contains("DIVIDEND YIELD")) {
                    detalhes.setDy(parseValor(valueStr));
                } else if (title.contains("P/VP")) {
                    detalhes.setPvp(parseValor(valueStr));
                } else if (title.contains("VARIAÇÃO")) {
                    detalhes.setVariacao12m(parseValor(valueStr));
                } else if (title.contains("LIQUIDEZ")) {
                    detalhes.setLiquidezDiaria(parseValor(valueStr));
                }
            }

            // Scrape Info Table (Segmento, Vacância, CNPJ, etc)
            Elements cells = doc.select("div#table-indicators div.cell");
            for (Element cell : cells) {
                String label = cell.select("span.name").text().toUpperCase();
                String valueStr = cell.select("div.value span").text();

                if (label.contains("SEGMENTO")) {
                    detalhes.setSetor(valueStr);
                } else if (label.contains("VACÂNCIA")) {
                    detalhes.setVacancia(parseValor(valueStr));
                } else if (label.contains("TIPO DE FUNDO")) {
                    detalhes.setTipoFundo(valueStr);
                } else if (label.contains("CNPJ")) {
                    detalhes.setCnpj(valueStr);
                } else if (label.contains("VAL. PATRIMONIAL P/ COTA")) {
                    detalhes.setValorPatrimonialPorCota(parseValor(valueStr));
                } else if (label.contains("NUMERO DE COTISTAS")) {
                    try {
                        String clean = valueStr.replaceAll("[^0-9]", "");
                        if (!clean.isEmpty())
                            detalhes.setNumeroCotistas(Integer.parseInt(clean));
                    } catch (Exception e) {
                    }
                }
            }

        } catch (Exception e) {
            log.error("Erro no crawler de detalhes para {}: {}", ticker, e.getMessage());
        }

        return detalhes;
    }
}
