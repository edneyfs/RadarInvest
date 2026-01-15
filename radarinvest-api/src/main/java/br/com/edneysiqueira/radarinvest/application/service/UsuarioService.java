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
                .build();

        Usuario salvo = repository.save(usuario);
        System.out.println("Usuário salvo com ID: " + salvo.getId());
        return salvo;
    }

    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
