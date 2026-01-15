package br.com.edneysiqueira.radarinvest.infrastructure.crawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FiiRuleDTO {
    private String name;
    private String ticker;
    private String segment;
    private int announcementDay;
    private int paymentDay;
}
