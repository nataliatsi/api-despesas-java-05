package com.github.progirls.despesas.api.despesas_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.progirls.despesas.api.despesas_api.security.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Autenticar usuário e gerar token", description = "Realiza a autenticação do usuário com credenciais básicas e retorna um token JWT válido para acesso às demais rotas protegidas da API.", responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida. Token retornado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Falha na autenticação. Credenciais inválidas ou ausentes.")
    })
    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    public ResponseEntity<?> authenticate(Authentication authentication) {
        String token = authenticationService.authenticate(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
