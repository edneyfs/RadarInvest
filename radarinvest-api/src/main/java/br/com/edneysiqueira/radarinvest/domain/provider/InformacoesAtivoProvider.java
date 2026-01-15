package br.com.edneysiqueira.radarinvest.domain.provider;

import br.com.edneysiqueira.radarinvest.domain.entity.DetalhesAtivo;

public interface InformacoesAtivoProvider {
    /**
     * Busca detalhes do ativo informado.
     * 
     * @param ticker    Ticker do ativo (ex: HGLG11)
     * @param tipoAtivo Tipo do ativo (ex: B3_FII)
     * @return Objeto DetalhesAtivo com as informações encontradas
     */
    DetalhesAtivo buscarDetalhes(String ticker, String tipoAtivo);
}
