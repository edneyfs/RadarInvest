package br.com.edneysiqueira.radarinvest.domain.repository;

import br.com.edneysiqueira.radarinvest.domain.entity.ItemWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ItemWatchlistRepository extends JpaRepository<ItemWatchlist, UUID> {
    Optional<ItemWatchlist> findByTicker(String ticker);
}
