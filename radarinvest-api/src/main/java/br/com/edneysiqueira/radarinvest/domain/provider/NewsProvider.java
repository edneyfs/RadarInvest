package br.com.edneysiqueira.radarinvest.domain.provider;

import br.com.edneysiqueira.radarinvest.api.dto.NewsDTO;
import java.util.List;

public interface NewsProvider {
    /**
     * Busca notícias ou fatos relevantes de um ativo.
     * @param ticker O código do ativo (ex: VALE3, HGLG11).
     * @param tipoAtivo O tipo do ativo (ACAO, FII, etc) para ajudar no roteamento.
     * @return Lista de notícias encontradas (pode ser paginada ou limitada pelo provider).
     */
    List<NewsDTO> buscarNoticias(String ticker, String tipoAtivo);
}
