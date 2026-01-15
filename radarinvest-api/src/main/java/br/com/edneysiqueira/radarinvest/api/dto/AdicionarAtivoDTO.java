package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados para adicionar um ativo à watchlist")
public class AdicionarAtivoDTO {

    @Schema(description = "Código do ativo na B3", example = "PETR4", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String ticker;

    @Schema(description = "Tipo do ativo", example = "B3_ACAO", allowableValues = { "B3_ACAO",
            "B3_FII" }, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String tipoAtivo; // B3_FII, B3_ACAO
}
