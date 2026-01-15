package br.com.edneysiqueira.radarinvest.domain.provider;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoNoticia;
import java.util.List;

public interface NoticiasProvider {
    /**
     * Busca notícias relevantes para o ativo.
     * 
     * @param ticker Ticker ativo
     * @return Lista de eventos de notícia
     */
    List<EventoNoticia> buscarNoticias(String ticker);
}
