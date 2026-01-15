package br.com.edneysiqueira.radarinvest.web.controller;

import br.com.edneysiqueira.radarinvest.application.service.AtivoService;
import br.com.edneysiqueira.radarinvest.domain.entity.DetalhesAtivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ativos")
@CrossOrigin(origins = "*")
public class AtivoController {

    private final AtivoService ativoService;

    @Autowired
    public AtivoController(AtivoService ativoService) {
        this.ativoService = ativoService;
    }

    @GetMapping("/{ticker}/detalhes")
    public DetalhesAtivo getDetalhes(@PathVariable String ticker,
            @RequestParam(name = "tipo", required = false) String tipoAtivo) {
        return ativoService.obterDetalhes(ticker, tipoAtivo);
    }
}
