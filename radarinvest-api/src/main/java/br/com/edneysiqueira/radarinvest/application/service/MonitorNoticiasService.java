package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoNoticia;
import br.com.edneysiqueira.radarinvest.domain.entity.ItemWatchlist;
import br.com.edneysiqueira.radarinvest.domain.provider.NoticiasProvider;
import br.com.edneysiqueira.radarinvest.domain.repository.EventoNoticiaRepository;
import br.com.edneysiqueira.radarinvest.domain.repository.ItemWatchlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitorNoticiasService {

    private final ItemWatchlistRepository watchlistRepository;
    private final EventoNoticiaRepository eventoNoticiaRepository;
    private final NoticiasProvider noticiasProvider;

    @Transactional
    public void executarMonitoramento() {
        log.info("Iniciando monitoramento de notícias (IA Agent)...");
        List<ItemWatchlist> itens = watchlistRepository.findAll();

        for (ItemWatchlist item : itens) {
            try {
                processarNoticias(item);
            } catch (Exception e) {
                log.error("Erro ao monitorar notícias para {}: {}", item.getTicker(), e.getMessage());
            }
        }
        log.info("Monitoramento de notícias finalizado.");
    }

    private void processarNoticias(ItemWatchlist item) {
        List<EventoNoticia> noticias = noticiasProvider.buscarNoticias(item.getTicker());

        for (EventoNoticia noticia : noticias) {
            // Deduplicação básica via HASH
            Optional<EventoNoticia> existente = eventoNoticiaRepository
                    .findByHashDeduplicacao(noticia.getHashDeduplicacao());

            if (existente.isEmpty()) {
                log.info("Nova notícia relevante para {}: [{}] {}", item.getTicker(), noticia.getSeveridade(),
                        noticia.getTitulo());
                eventoNoticiaRepository.save(noticia);
            }
        }
    }
}
