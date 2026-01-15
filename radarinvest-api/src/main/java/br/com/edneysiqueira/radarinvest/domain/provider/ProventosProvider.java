package br.com.edneysiqueira.radarinvest.domain.provider;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import java.util.List;

public interface ProventosProvider {
    /**
     * Busca lista de proventos relevantes para o ativo informado.
     * 
     * @param ticker    Ticker do ativo (ex: HGLG11)
     * @param tipoAtivo Tipo do ativo (ex: B3_FII)
     * @return Lista de eventos encontrados
     */
    List<EventoAtivo> buscarProventos(String ticker, String tipoAtivo);
}
