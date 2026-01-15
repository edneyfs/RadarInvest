package br.com.edneysiqueira.radarinvest.infrastructure.brapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrapiResponse {
    private List<BrapiResult> results;
}
