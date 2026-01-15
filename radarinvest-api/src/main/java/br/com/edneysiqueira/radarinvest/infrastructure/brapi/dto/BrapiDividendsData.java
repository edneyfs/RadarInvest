package br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class BrapiDividendsData {
    @JsonProperty("cashDividends")
    private List<BrapiCashDividend> cashDividends;
}
