package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Notícia ou Fato Relevante de um ativo")
public record NewsDTO(
        @Schema(description = "Descrição ou título da notícia", example = "Relatório Gerencial")
        String descricao,

        @Schema(description = "Data de publicação", example = "2026-01-14")
        @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate data,

        @Schema(description = "Link para acessar a notícia completa", example = "https://investidor10.com.br/...")
        String link
) {}
