package com.github.progirls.despesas.api.despesas_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.service.DespesaService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users/depesa")
public class DespesaController {
    
    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    public ResponseEntity<?> criarNovaDespesa(@RequestBody @Valid DespesaRegisterDto registerDto, JwtAuthenticationToken token) {

        try {
            despesaService.adicionarDespesa(registerDto, token);
            return ResponseEntity.status(HttpStatus.CREATED).body("Despesa criada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro inesperado: " + e.getMessage());
        }
    }
}
