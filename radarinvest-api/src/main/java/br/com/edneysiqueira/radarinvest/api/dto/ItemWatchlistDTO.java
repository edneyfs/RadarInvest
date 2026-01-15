package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Item da lista de monitoramento (Watchlist)")
public class ItemWatchlistDTO {

    @Schema(description = "Código do ativo", example = "VALE3")
    private String ticker;

    @Schema(description = "Tipo do ativo", example = "B3_ACAO")
    private String tipoAtivo;

    @Schema(description = "Lista de proventos futuros previstos")
    // Lista de Proventos Futuros
    private java.util.List<ProventoDTO> proventos;

    // Calculado
    @Schema(description = "Data sugerida para compra baseada na data-com mais próxima", example = "2023-10-14")
    private LocalDate melhorDiaCompra;

    @Schema(description = "Explicação da sugestão de compra", example = "Compre antes de 15/10 par garantir os dividendos")
    private String mensagemMelhorDia;
}
