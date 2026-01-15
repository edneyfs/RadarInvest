package br.com.edneysiqueira.radarinvest.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evento_ativo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoAtivo {

    @Id
    private UUID id;

    private String identificadorAtivo; // Ticker
    private String tipoAtivo;
    private String tipoEvento; // PROVENTO, OUTRO

    private LocalDate dataCom;
    private LocalDate dataEx;
    private LocalDate dataPagamento;

    private BigDecimal valor;
    private String moeda;

    private String fonte;
    private LocalDateTime coletadoEm;

    private String dadosExtrasJson;

}
