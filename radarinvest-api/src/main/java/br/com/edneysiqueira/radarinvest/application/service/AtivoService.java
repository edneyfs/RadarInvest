package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.domain.entity.DetalhesAtivo;
import br.com.edneysiqueira.radarinvest.domain.provider.InformacoesAtivoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtivoService {

    private final InformacoesAtivoProvider informacoesAtivoProvider;

    @Autowired
    public AtivoService(InformacoesAtivoProvider informacoesAtivoProvider) {
        this.informacoesAtivoProvider = informacoesAtivoProvider;
    }

    public DetalhesAtivo obterDetalhes(String ticker, String tipoAtivo) {
        // Fallback for tipoAtivo if not provided or just guess
        if (tipoAtivo == null || tipoAtivo.isEmpty()) {
            // Simplification: try to guess or let provider handle default (acoes)
            // But Controller should pass it.
        }
        return informacoesAtivoProvider.buscarDetalhes(ticker, tipoAtivo);
    }
}
