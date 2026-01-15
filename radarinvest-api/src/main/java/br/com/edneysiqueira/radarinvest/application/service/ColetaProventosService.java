package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import br.com.edneysiqueira.radarinvest.domain.entity.ItemWatchlist;
import br.com.edneysiqueira.radarinvest.domain.provider.ProventosProvider;
import br.com.edneysiqueira.radarinvest.domain.repository.EventoAtivoRepository;
import br.com.edneysiqueira.radarinvest.domain.repository.ItemWatchlistRepository;
import br.com.edneysiqueira.radarinvest.infrastructure.crawler.FiiHubCrawlerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ColetaProventosService {

    private final ItemWatchlistRepository watchlistRepository;
    private final EventoAtivoRepository eventoAtivoRepository;
    private final ProventosProvider proventosProvider;
    private final FiiHubCrawlerProvider fiiHubCrawlerProvider;

    @Transactional
    public void executarColetaGeral() {
        log.info("Iniciando coleta geral de proventos...");
        List<ItemWatchlist> itens = watchlistRepository.findAll();

        for (ItemWatchlist item : itens) {
            try {
                processarItem(item);
            } catch (Exception e) {
                log.error("Erro ao processar item {}: {}", item.getTicker(), e.getMessage());
            }
        }
        log.info("Coleta geral finalizada.");
    }

    public void processarItem(ItemWatchlist item) {
        List<EventoAtivo> eventos = proventosProvider.buscarProventos(item.getTicker(), item.getTipoAtivo());

        if (!eventos.isEmpty()) {
            log.info("Encontrados {} eventos para {}", eventos.size(), item.getTicker());

            // Para V1, simples: salva todos.
            // V1.1 poderia verificar duplicatas mais rigorosamente.
            // Como EventoAtivo tem ID random no crawler, sempre vai inserir novo se nao
            // checar campos.
            // Vamos checar duplicidade básica: DataCom + Valor + Tipo

            List<EventoAtivo> existentes = eventoAtivoRepository.findAllByTicker(item.getTicker());

            for (EventoAtivo novo : eventos) {
                boolean existe = existentes.stream().anyMatch(e -> areEventsEqual(e, novo));

                if (!existe) {
                    eventoAtivoRepository.save(novo);
                }
            }

        } else {
            log.info("Nenhum provento encontrado para {}", item.getTicker());

            // Fallback para FIIs usando FiiHub
            if ("B3_FII".equalsIgnoreCase(item.getTipoAtivo())) {
                log.info("Tentando fallback FiiHub para {}", item.getTicker());
                fiiHubCrawlerProvider.getProventoDate(item.getTicker()).ifPresent(dataCom -> {
                    EventoAtivo evento = EventoAtivo.builder()
                            .id(UUID.randomUUID())
                            .identificadorAtivo(item.getTicker())
                            .tipoAtivo(item.getTipoAtivo())
                            .tipoEvento("Rendimento (Projetado)")
                            .dataCom(dataCom)
                            .valor(BigDecimal.ZERO) // Valor desconhecido
                            .moeda("BRL")
                            .fonte("FiiHub (Projetado)")
                            .coletadoEm(LocalDateTime.now())
                            .dadosExtrasJson("{\"info\": \"Data estimada baseada em regra de recorrência do FiiHub\"}")
                            .build();

                    // Verifica se já existe algo similar (mesma data com)
                    boolean existe = eventoAtivoRepository.findAllByTicker(item.getTicker()).stream()
                            .anyMatch(e -> e.getDataCom() != null && e.getDataCom().isEqual(dataCom));

                    if (!existe) {
                        eventoAtivoRepository.save(evento);
                        log.info("Evento projetado salvo para {}: DataCom {}", item.getTicker(), dataCom);
                    }
                });
            }
        }
    }

    private boolean areEventsEqual(EventoAtivo e1, EventoAtivo e2) {
        // Compara Data Com (ou Pag se Com null), Valor e Tipo
        // Ignora ID
        if (e1.getValor().compareTo(e2.getValor()) != 0)
            return false;
        if (!e1.getTipoEvento().equalsIgnoreCase(e2.getTipoEvento()))
            return false;

        LocalDate d1 = e1.getDataCom() != null ? e1.getDataCom() : e1.getDataPagamento();
        LocalDate d2 = e2.getDataCom() != null ? e2.getDataCom() : e2.getDataPagamento();

        if (d1 == null && d2 == null)
            return true;
        if (d1 == null || d2 == null)
            return false;

        return d1.isEqual(d2);
    }
}
