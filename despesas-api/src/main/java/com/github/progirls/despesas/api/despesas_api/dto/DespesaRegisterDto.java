package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DespesaRegisterDto(
    @NotNull(message = "O valor deve ser inserido.")
    Double valor,

    @NotBlank(message = "Por favor insira uma descrição.")
    @Size(max = 255)
    String descricao,

    LocalDateTime dataInicio,
    
    LocalDateTime dataFim,
    
    @NotNull(message = "O número de parcelas deve ser informado.")
    Integer parcelamento,
 
    @NotNull(message = "Informe se a depesa está quitada ou não.")
    Boolean quitado
    ) {
}