package br.com.edneysiqueira.radarinvest.domain.repository;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoAtivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoAtivoRepository extends JpaRepository<EventoAtivo, UUID> {

    // Find latest event (e.g. latest dataPagamento or dataEx)
    @Query("SELECT e FROM EventoAtivo e WHERE e.identificadorAtivo = :ticker ORDER BY e.dataPagamento DESC, e.dataEx DESC LIMIT 1")
    Optional<EventoAtivo> findTopByTickerOrderByData(String ticker);

    // Alias para uso no Service
    default List<EventoAtivo> findAllByTicker(String ticker) {
        return findByIdentificadorAtivo(ticker);
    }

    // JPA Method
    List<EventoAtivo> findByIdentificadorAtivo(String ticker);
}
