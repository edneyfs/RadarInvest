package br.com.edneysiqueira.radarinvest.api.controller;

import br.com.edneysiqueira.radarinvest.api.dto.UsuarioDTO;
import br.com.edneysiqueira.radarinvest.application.service.UsuarioService;
import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gestão de cadastro de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Operation(summary = "Cadastrar novo usuário", description = "Cria uma nova conta de usuário no sistema. A senha será armazenada de forma criptografada.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (CPF inválido, Senha fraca, Email duplicado)")
    })
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid UsuarioDTO dados, UriComponentsBuilder uriBuilder) {
        Usuario usuario = service.salvar(dados);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
