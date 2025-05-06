package com.github.progirls.despesas.api.despesas_api.controller;

import com.github.progirls.despesas.api.despesas_api.dto.DespesaFiltradaDTO;
import com.github.progirls.despesas.api.despesas_api.dto.ListaDespesasResponseDTO;
import com.github.progirls.despesas.api.despesas_api.dto.NovaDespesaDTO;
import com.github.progirls.despesas.api.despesas_api.service.DespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

            return ResponseEntity.badRequest().body("Erro inesperado: " + e.getMessage());
        }

    }

    @Operation(
            summary = "Buscar despesas com filtros (categoria e/ou datas)",
            description = "Retorna uma lista de despesas filtradas por categoria e/ou intervalo de datas. Todos os filtros são opcionais.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Despesas encontradas com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ListaDespesasResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<ListaDespesasResponseDTO> buscarDespesasComFiltros(
            @Parameter(description = "Categoria da despesa (opcional)")
            @RequestParam(required = false) String categoria,

            @Parameter(description = "Data de fim no formato ISO (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,

            @Parameter(description = "Data de início no formato ISO (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Authentication authentication
    ) {
        List<DespesaFiltradaDTO> despesas = despesaService.buscarDespesasComFiltros(categoria, dataInicio, dataFim, authentication);

        String mensagem = despesas.isEmpty()
                ? "Nenhuma despesa encontrada com os filtros fornecidos."
                : "Despesas encontradas com sucesso.";

        ListaDespesasResponseDTO resposta = new ListaDespesasResponseDTO(mensagem, despesas);
        return ResponseEntity.ok(resposta);
    }

}
