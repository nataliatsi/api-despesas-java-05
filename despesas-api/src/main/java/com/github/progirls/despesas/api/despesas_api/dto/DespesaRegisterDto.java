package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DespesaRegisterDto(

    @NotNull(message = "O valor deve ser inserido.")
    Double valor,

    @NotBlank(message = "A descrição deve ser inserida.")
    @Size(max = 255)
    String descricao,

    LocalDate dataInicio,
    
    LocalDate dataFim,

    @NotNull(message = "O parcelamento deve ser inserido.")
    Integer parcelamento,

    @NotNull(message = "Informe se a despesa está quitada ou não.")
    Boolean quitado
) {
}
