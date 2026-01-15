package br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BrapiCashDividend {
    private BigDecimal rate;
    private String relatedTo;

    @JsonProperty("paymentDate")
    private String paymentDate; // ISO format

    @JsonProperty("approvedOn")
    private String approvedOn;

    @JsonProperty("label")
    private String label; // DIVIDENDO, JCP

    @JsonProperty("lastDatePrior")
    private String lastDatePrior; // Data Com
}
