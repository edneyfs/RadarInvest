package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    // Chave secreta usada para assinar o token. Ninguém deve saber isso além da
    // API.
    @Value("${jwt.secret:segredo_super_secreto_para_desenvolvimento_apenas_123456}")
    private String secret;

    // Tempo de expiração do token em milissegundos
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    // Gera a chave criptográfica baseada no segredo configurado
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Gera um novo token JWT para o usuário autenticado.
     * O token contém o ID do usuário (Subject), data de criação e expiração.
     * É assinado digitalmente com o algoritmo HS256 para garantir integridade.
     */
    public String gerarToken(Usuario usuario) {
        String role = usuario.getRole();
        if (role == null) {
            role = "USER";
        }
        
        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida se um token JWT é autêntico e não expirou.
     * Retorna true se a assinatura bater com a chave secreta e o tempo for válido.
     */
    public boolean isTokenValido(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai o ID do usuário (Subject) de dentro do token.
     */
    public String getIdUsuario(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
