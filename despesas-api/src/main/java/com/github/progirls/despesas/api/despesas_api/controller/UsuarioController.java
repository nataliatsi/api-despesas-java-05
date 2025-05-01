package com.github.progirls.despesas.api.despesas_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.progirls.despesas.api.despesas_api.dto.UsuarioRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Usuario;
import com.github.progirls.despesas.api.despesas_api.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/users")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    private UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    // Rota para o cadastro de novos usuários
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid UsuarioRegisterDto dto) {
        
        // Try e Catch para a criação de um novo usuário caso ocorra um erro
        try {
            usuarioService.criarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
        } catch (ResponseStatusException e) {

            // Retorna o status e a mensagem original da exceção
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
            
        } catch (Exception e) {
            // Outros erros inesperados continuam como Bad Request
            return ResponseEntity.badRequest().body("Erro inesperado: " + e.getMessage());
        } 
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> pegarUsuarios() {
        return ResponseEntity.status(200).body(usuarioService.listarUsuarios());
    }
    
}
