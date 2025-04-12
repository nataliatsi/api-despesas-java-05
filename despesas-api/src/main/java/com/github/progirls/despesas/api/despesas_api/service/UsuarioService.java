package com.github.progirls.despesas.api.despesas_api.service;

import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // Criptografa a senha
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        // Cria um novo objeto Usuario corretamente
        Usuario novoUsuario = new Usuario(
            UUID.randomUUID(),                    // Gera um novo ID único
            usuario.getNome(),
            usuario.getEmail(),
            senhaCriptografada,
            LocalDateTime.now()
        );

        return usuarioRepository.save(novoUsuario);
    }
}