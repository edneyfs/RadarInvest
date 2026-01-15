package br.com.edneysiqueira.radarinvest.domain.repository;

import br.com.edneysiqueira.radarinvest.domain.entity.EventoNoticia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventoNoticiaRepository extends JpaRepository<EventoNoticia, UUID> {

    Optional<EventoNoticia> findByHashDeduplicacao(String hash);

    List<EventoNoticia> findByIdentificadorAtivoOrderByPublicadoEmDesc(String ticker);
}
