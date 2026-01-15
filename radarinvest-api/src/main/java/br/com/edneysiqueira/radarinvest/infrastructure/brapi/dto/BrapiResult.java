package br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BrapiResult {
    private String symbol;
    private String currency;
    private Double regularMarketPrice;

    @JsonProperty("dividendsData")
    private BrapiDividendsData dividendsData;
}
