package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDateTime;

public record GetDespesaDto(
    Double valor,
    String descricao,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    Integer parcelamento,
    Boolean quitado
 
) {    
}
