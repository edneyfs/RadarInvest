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
@Table(name = "evento_noticia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoNoticia {

    @Id
    private UUID id;

    private String identificadorAtivo;
    private String tipoAtivo;

    private String titulo;
    private String resumo;
    private String url;

    private LocalDateTime publicadoEm;
    private String categoria; // FAT_RELEVANTE, RESULTADOS, etc.
    private String severidade; // BAIXO, MEDIO, ALTO

    private String fonte;
    private LocalDateTime coletadoEm;
    private String hashDeduplicacao;

}
