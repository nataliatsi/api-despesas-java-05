package com.github.progirls.despesas.api.despesas_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaRegisterDto;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaResponseDto;
import com.github.progirls.despesas.api.despesas_api.entities.Despesa;
import com.github.progirls.despesas.api.despesas_api.mapper.DespesaMapper;
import com.github.progirls.despesas.api.despesas_api.service.DespesaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/users/despesas")
public class DespesaController {

    private final DespesaService despesaService;
    private final DespesaMapper despesaMapper;

    public DespesaController(DespesaService despesaService, DespesaMapper despesaMapper) {
        this.despesaService = despesaService;
        this.despesaMapper = despesaMapper;
    }

    @Operation(summary = "Adicionar nova despesa",
        description = "Cria uma nova despesa vinculada ao usuário autenticado.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
        })
    @PostMapping
    public ResponseEntity<DespesaResponseDto> adicionarDespesa(@Valid @RequestBody DespesaRegisterDto dto, JwtAuthenticationToken token){
        Despesa despesa = despesaService.adicionarDespesa(dto, token);
        DespesaResponseDto response = despesaMapper.toResponseDto(despesa);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }


    @GetMapping
    public ResponseEntity<List<Despesa>> pegarDespesas() {
        return ResponseEntity.status(200).body(despesaService.listarDespesas());
    }
}
