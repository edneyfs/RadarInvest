package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para autenticação")
public record LoginDTO(
                @Schema(description = "E-mail cadastrado", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank @Email String email,

                @Schema(description = "Senha de acesso", example = "S3nhaForte!", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String senha) {
}
