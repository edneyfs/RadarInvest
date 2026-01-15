package br.com.edneysiqueira.radarinvest.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

// Using record for DTO in Java 21
@Schema(description = "Dados para cadastro de novo usuário")
public record UsuarioDTO(
                @Schema(description = "Nome completo do usuário", example = "Fulano de Tal", requiredMode = Schema.RequiredMode.REQUIRED) 
				@NotBlank 
				String nome,

                @Schema(description = "CPF do usuário (apenas números ou formatado)", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED) 
				@NotBlank 
				@CPF 
				String cpf,

                @Schema(description = "Endereço de e-mail válido", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED) 
				@NotBlank 
				@Email 
				String email,

                @Schema(description = "Senha forte (min 8 caracteres)", example = "S3nhaForte!", requiredMode = Schema.RequiredMode.REQUIRED) 
				@NotBlank 
				String senha) {
}
