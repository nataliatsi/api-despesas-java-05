package com.github.progirls.despesas.api.despesas_api.controller;

import com.github.progirls.despesas.api.despesas_api.dto.AtualizarDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.DespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.PageResponseDTO;
import com.github.progirls.despesas.api.despesas_api.service.DespesaService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return ResponseEntity.status(HttpStatus.CREATED).body(novaDespesa);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro inesperado: "  +e.getMessage());
        }

    }

    @Operation(summary = "Listar as despesas do usuário autenticado",

            description = "Este endpoint permite que o usuário liste todas as suas despesas já criadas. " +
                    "O usuário precisa estar autenticado para mostrar a sua lista. ", responses = {
            @ApiResponse(responseCode = "200", description = "Lista devolvida com sucesso."),
            @ApiResponse(responseCode = "401", description = "Falha na autenticação. Token inválido ou ausente.")})
    @GetMapping
    public ResponseEntity<?> listarDespesas(@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "6") int tamanho, Authentication authentication) {

        try {
            Pageable pageable = PageRequest.of(pagina, tamanho);
            Page<DespesaDTO> despesaPage = despesaService.listarDespesaDoUsuario(authentication, pageable);

            return ResponseEntity.ok(new PageResponseDTO<>(despesaPage));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDespesa(@PathVariable Long id, 
            @RequestBody @Valid AtualizarDespesaDTO dto, Authentication authentication) {

        try {
            DespesaDTO atualizada = despesaService.editarDespesa(authentication, id, dto);
            return ResponseEntity.ok(atualizada);

        } catch (EntityNotFoundException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Despesa não encontrada");
        } catch (AccessDeniedException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado, token inválido");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar despesa: " + e.getMessage());
        }
    }
}
