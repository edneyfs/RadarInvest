package br.com.edneysiqueira.radarinvest.application.service;

import br.com.edneysiqueira.radarinvest.api.dto.UsuarioDTO;
import br.com.edneysiqueira.radarinvest.domain.entity.Usuario;
import br.com.edneysiqueira.radarinvest.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // We need to configure this bean
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(UsuarioDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        System.out.println("Tentando salvar usuário: " + dto.email());
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .role("USER")
                .build();

        Usuario salvo = repository.save(usuario);
        System.out.println("Usuário salvo com ID: " + salvo.getId());
        return salvo;
    }

    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void updateLastSeen(String email) {
        repository.findByEmail(email).ifPresent(user -> {
            user.setLastSeen(java.time.LocalDateTime.now());
            repository.save(user);
        });
    }

    @jakarta.annotation.PostConstruct
    public void ensureAdminUser() {
        if (repository.findByEmail("admin").isEmpty()) {
            System.out.println("Criando usuário ADMIN padrão...");
            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .cpf("00000000000")
                    .email("admin")
                    .senha(passwordEncoder.encode("2904@Arthur"))
                    .role("ADMIN")
                    .build();
            repository.save(admin);
        }
    }
}
