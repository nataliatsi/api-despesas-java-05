package com.github.progirls.despesas.api.despesas_api.controller;

import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @Operation(summary = "Criar uma nova despesa ao usuário autenticado",

            description = "Este endpoint permite que o usuário crie uma despesa nova. " +
                    "O usuário precisa estar autenticado para realizar a criação. ", responses = {
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Despesa com dados incompletos ou inválidos."),
            @ApiResponse(responseCode = "401", description = "Falha na autenticação. Token inválido ou ausente.")})
    @PostMapping
    public ResponseEntity<?> criarDespesa(@Valid @RequestBody NovaDespesaDTO dto, Authentication authentication) {
        try {
            var novaDespesa = despesaService.criarDespesa(dto, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body("Despesa Criada com sucesso " + novaDespesa);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro inesperado: "  +e.getMessage());
        }

    }
}
