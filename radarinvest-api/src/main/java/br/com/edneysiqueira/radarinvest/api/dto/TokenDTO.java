package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token de autenticação gerado")
public record TokenDTO(
        @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiJ9...") String token,

        @Schema(description = "Tipo do token", example = "Bearer") String tipo,

        @Schema(description = "Nome do usuário autenticado", example = "Fulano de Tal") String nome) {
    public TokenDTO(String token, String nome) {
        this(token, "Bearer", nome);
    }
}
