package br.com.edneysiqueira.radarinvest.web.controller;

import br.com.edneysiqueira.radarinvest.api.dto.AdicionarAtivoDTO;
import br.com.edneysiqueira.radarinvest.api.dto.ItemWatchlistDTO;
import br.com.edneysiqueira.radarinvest.application.service.WatchlistService;
import br.com.edneysiqueira.radarinvest.domain.entity.EventoNoticia;
import br.com.edneysiqueira.radarinvest.domain.repository.EventoNoticiaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para facilitar V1 dev
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final EventoNoticiaRepository eventoNoticiaRepository;

    @GetMapping
    public List<ItemWatchlistDTO> listar() {
        return watchlistService.listarWatchlist();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionar(@RequestBody @Valid AdicionarAtivoDTO dto) {
        watchlistService.adicionarAtivo(dto);
    }

    @DeleteMapping("/{ticker}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable String ticker) {
        watchlistService.removerAtivo(ticker);
    }

    @GetMapping("/{ticker}/noticias")
    public List<EventoNoticia> listarNoticias(@PathVariable String ticker) {
        return eventoNoticiaRepository.findByIdentificadorAtivoOrderByPublicadoEmDesc(ticker);
    }
}
