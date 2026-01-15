package br.com.edneysiqueira.radarinvest.api.controller;

import br.com.edneysiqueira.radarinvest.api.dto.LoginDTO;
import br.com.edneysiqueira.radarinvest.api.dto.TokenDTO;
import br.com.edneysiqueira.radarinvest.application.service.TokenService;
import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e gestão de tokens")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Autenticar usuário", description = "Valida as credenciais (email/senha) e retorna um token JWT para acesso aos recursos protegidos.", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Retorna o Token JWT."),
            @ApiResponse(responseCode = "400", description = "Dados de login inválidos"),
            @ApiResponse(responseCode = "403", description = "Conta bloqueada ou credenciais erradas")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid LoginDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        Usuario usuario = (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(new TokenDTO(tokenJWT, usuario.getNome()));
    }
}
