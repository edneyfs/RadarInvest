package br.com.edneysiqueira.radarinvest.api.controller;

import br.com.edneysiqueira.radarinvest.api.dto.NewsDTO;
import br.com.edneysiqueira.radarinvest.domain.provider.NewsProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "Notícias e Fatos Relevantes")
public class NewsController {

    private final NewsProvider newsProvider;

    @GetMapping("/{ticker}")
    @Operation(summary = "Buscar notícias de um ativo", description = "Retorna os últimos fatos relevantes e comunicados (últimos 3 meses).")
    public List<NewsDTO> getNews(@PathVariable String ticker, 
                                 @RequestParam(defaultValue = "ACAO") String tipoAtivo) {
        // Normaliza tipoAtivo se necessário, mas o provider lida com B3_FII
        return newsProvider.buscarNoticias(ticker, tipoAtivo);
    }
}
