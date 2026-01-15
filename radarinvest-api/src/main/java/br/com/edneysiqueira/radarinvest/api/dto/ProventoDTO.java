package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Detalhes de um provento (dividendo/JCP)")
public class ProventoDTO {

    @Schema(description = "Tipo de provento", example = "DIVIDENDO", allowableValues = { "DIVIDENDO", "JCP" })
    private String tipo;

    @Schema(description = "Valor do provento por ação", example = "0.45")
    private BigDecimal valor;

    @Schema(description = "Data limite para ter o ativo na carteira (Data Com)", example = "2023-10-15")
    private LocalDate dataCom;

    @Schema(description = "Data prevista para o pagamento", example = "2023-11-30")
    private LocalDate dataPagamento;
}
