package br.com.edneysiqueira.radarinvest.infrastructure.config;

import br.com.edneysiqueira.radarinvest.application.service.TokenService;
import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import br.com.edneysiqueira.radarinvest.domain.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Filtro que intercepta TODAS as requisições HTTP antes de chegarem aos
 * controllers.
 * Objetivo: Verificar se existe um cabeçalho "Authorization" com um token JWT
 * válido.
 * Se o token for válido, autentica o usuário no contexto do Spring Security.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recuperarToken(request);

        // Se existe token e ele é válido...
        if (token != null && tokenService.isTokenValido(token)) {
            // ...recuperamos o ID do usuário e buscamos no banco.
            String idUsuario = tokenService.getIdUsuario(token);
            repository.findById(UUID.fromString(idUsuario)).ifPresent(usuario -> {
                // OTIMIZAÇÃO: Só atualiza o banco se passou mais de 5 minutos desde a última vez
                // Isso evita um UPDATE no banco a cada requisição HTTP (Performance)
                var minutes = java.time.LocalDateTime.now().minusMinutes(9);
                if (usuario.getLastSeen() == null || usuario.getLastSeen().isBefore(minutes)) {
                    usuario.setLastSeen(java.time.LocalDateTime.now());
                    repository.save(usuario);
                }

                // Forçamos a autenticação do usuário para esta requisição específica
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }
        // Segue o fluxo normal da requisição (vai para o próximo filtro ou controller)
        filterChain.doFilter(request, response);
    }

    // Extrai o token do Header "Authorization: Bearer <token>"
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
