package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.api.dto.AdicionarAtivoDTO;
import br.com.edneysiqueira.radarinvest.api.dto.ItemWatchlistDTO;
import br.com.edneysiqueira.radarinvest.api.dto.ProventoDTO;
import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import br.com.edneysiqueira.radarinvest.domain.entity.ItemWatchlist;
import br.com.edneysiqueira.radarinvest.domain.repository.EventoAtivoRepository;
import br.com.edneysiqueira.radarinvest.domain.repository.ItemWatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final ItemWatchlistRepository watchlistRepository;
    private final EventoAtivoRepository eventoAtivoRepository;
    private final ColetaProventosService coletaProventosService;

    @Transactional(readOnly = true)
    public List<ItemWatchlistDTO> listarWatchlist() {
        return watchlistRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void adicionarAtivo(AdicionarAtivoDTO dto) {
        if (watchlistRepository.findByTicker(dto.getTicker()).isPresent()) {
            throw new IllegalArgumentException("Ativo já está na watchlist.");
        }

        ItemWatchlist item = ItemWatchlist.builder()
                .id(UUID.randomUUID())
                .ticker(dto.getTicker().toUpperCase())
                .tipoAtivo(dto.getTipoAtivo())
                .criadoEm(LocalDateTime.now())
                .build();

        watchlistRepository.save(item);

        try {
            coletaProventosService.processarItem(item);
        } catch (Exception e) {
            // Ignora falha na coleta inicial
        }
    }

    @Transactional
    public void removerAtivo(String ticker) {
        watchlistRepository.findByTicker(ticker).ifPresent(watchlistRepository::delete);
    }

    private ItemWatchlistDTO converterParaDTO(ItemWatchlist item) {
        // Agora buscamos TODOS os eventos para esse ticker
        // (idealmente filtrando por data > X para não trazer histórico infinito, mas
        // aqui trazemos "top 10" ou todos recentes)
        List<EventoAtivo> eventos = eventoAtivoRepository.findAllByTicker(item.getTicker());

        // Filtra para garantir apenas futuros ou recentíssimos
        LocalDate hoje = LocalDate.now();
        List<ProventoDTO> proventosDTO = eventos.stream()
                .filter(e -> e.getDataCom().isAfter(hoje.minusDays(30))
                        || (e.getDataPagamento() != null && e.getDataPagamento().isAfter(hoje)))
                .sorted(Comparator.comparing(EventoAtivo::getDataCom).reversed()) // Mais recentes no topo
                .map(e -> ProventoDTO.builder()
                        .tipo(e.getTipoEvento())
                        .valor(e.getValor())
                        .dataCom(e.getDataCom())
                        .dataPagamento(e.getDataPagamento())
                        .build())
                .collect(Collectors.toList());

        ItemWatchlistDTO.ItemWatchlistDTOBuilder builder = ItemWatchlistDTO.builder()
                .ticker(item.getTicker())
                .tipoAtivo(item.getTipoAtivo())
                .proventos(proventosDTO);

        // Lógica Melhor Dia (Baseado no próximo Data Com futuro)
        Optional<ProventoDTO> proximoCom = proventosDTO.stream()
                .filter(p -> p.getDataCom().isAfter(hoje) || p.getDataCom().equals(hoje))
                .min(Comparator.comparing(ProventoDTO::getDataCom));

        if (proximoCom.isPresent()) {
            LocalDate dataCom = proximoCom.get().getDataCom();
            // Melhor dia para comprar é a própria data com (ou Ex - 1 se tivesse esse dado
            // confiável)
            LocalDate melhorDia = dataCom;

            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = melhorDia.format(formatter);

            builder.melhorDiaCompra(melhorDia);
            builder.mensagemMelhorDia("Compre até " + dataFormatada);
        } else {
            // Se não tem futuro, verifica se expirou recentemente
            boolean expiradoRecentemente = proventosDTO.stream()
                    .anyMatch(p -> p.getDataCom().isBefore(hoje) && p.getDataPagamento().isAfter(hoje));

            if (expiradoRecentemente) {
                builder.mensagemMelhorDia("Data limite expirada.");
            } else {
                builder.mensagemMelhorDia("Sem oportunidades à vista.");
            }
        }

        return builder.build();
    }
}
