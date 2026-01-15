package br.com.edneysiqueira.radarinvest.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_watchlist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemWatchlist {

    @Id
    private UUID id;

    private String ticker;
    private String tipoAtivo; // B3_FII, B3_ACAO
    private LocalDateTime criadoEm;

}
