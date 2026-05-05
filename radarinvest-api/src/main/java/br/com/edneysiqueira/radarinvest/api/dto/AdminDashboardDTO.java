package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminDashboardDTO(
    @Schema(description = "Total de usuários cadastrados")
    long totalUsers,
    
    @Schema(description = "Usuários online (vistos nos últimos 10 min)")
    long onlineUsers,
    
    @Schema(description = "Total de ativos sendo monitorados (distintos)")
    long monitoredTickers
) {}
