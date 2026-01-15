package br.com.edneysiqueira.radarinvest.web.controller;

import br.com.edneysiqueira.radarinvest.application.job.AgendadorColeta;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atualizar")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AgendadorColeta agendadorColeta;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void forcarAtualizacao() {
        // Roda em background ou foreground?
        // Para feedback imediato na demo, vamos rodar na thread se for rapido.
        // Mas a boa prática é async. Como são mock providers rapidos:
        agendadorColeta.agendarColetaProventos();
    }
}
