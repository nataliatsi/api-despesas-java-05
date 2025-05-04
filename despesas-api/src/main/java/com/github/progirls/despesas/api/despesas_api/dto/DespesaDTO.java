package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDate;

public record DespesaDTO(
        Long id,
        UsuarioDTO usuario,
        Double valor,
        String descricao,
        Integer parcelamento,
        LocalDate dataInicio,
        LocalDate dataFim,
        Boolean quitado) {
}
