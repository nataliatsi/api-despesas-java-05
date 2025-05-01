package com.github.progirls.despesas.api.despesas_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.progirls.despesas.api.despesas_api.dto.UsuarioRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

@Service
public class UsuarioService {
    
    private BCryptPasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final FindUser findUser;

    public UsuarioService(BCryptPasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository, FindUser findUser) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.findUser = findUser;
    }
    
    public Usuario criarUsuario(UsuarioRegisterDto usuarioRegisterDto) {
        
        var usuarioEmail = usuarioRepository.findByEmail(usuarioRegisterDto.email());
        if (usuarioEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado, por favor tente outro.");
        }
        
        String encryptedPassword = passwordEncoder.encode(usuarioRegisterDto.senha());

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRegisterDto.nome());
        usuario.setSenha(encryptedPassword);
        usuario.setEmail(usuarioRegisterDto.email());
        usuario.setDataCriacao(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        return usuario;

    }

    // Retirar antes de enviar o PR!!!!
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

}
