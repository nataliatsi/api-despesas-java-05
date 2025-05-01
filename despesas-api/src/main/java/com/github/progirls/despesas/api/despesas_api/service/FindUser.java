package com.github.progirls.despesas.api.despesas_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.repository.UsuarioRepository;

@Service
public class FindUser {
    
    private final UsuarioRepository usuarioRepository;

    public FindUser(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario pegarUsuarioAutenticado(Authentication authentication) {
        String username = authentication.getName();

        return usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não"));
    }
}
