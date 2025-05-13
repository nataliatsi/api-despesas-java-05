package com.github.progirls.despesas.api.despesas_api.dto;

import java.util.List;

public record ListaDespesasResponseDTO(
        String mensagem,
        List<DespesaFiltradaDTO> despesas
) {
}
