package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDate;

public record GetDespesaDto(
    Double valor,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer parcelamento,
    Boolean quitado
) {
}
