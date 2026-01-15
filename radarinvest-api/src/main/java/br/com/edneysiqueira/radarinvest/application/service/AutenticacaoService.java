package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import br.com.edneysiqueira.radarinvest.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Tentando autenticar: " + username);
        Optional<Usuario> usuario = repository.findByEmail(username);
        if (usuario.isPresent()) {
            System.out.println("Usuário encontrado no banco.");
            // Adapting Usuario to Spring Security UserDetails simply
            System.out.println("Usuário encontrado no banco.");
            // Agora Usuario implementa UserDetails, então podemos retorná-lo diretamente
            Usuario u = usuario.get();
            System.out.println("Senha hash no banco: " + u.getSenha());
            return u;
        }
        System.out.println("Usuário NÃO encontrado no banco.");
        throw new UsernameNotFoundException("Dados inválidos");
    }
}
