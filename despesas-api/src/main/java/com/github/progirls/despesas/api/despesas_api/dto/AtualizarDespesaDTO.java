package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AtualizarDespesaDTO(
    @NotBlank(message = "A categoria da despesa é obrigatória")
    @Size(max = 55, message = "A descrição deve ter no máximo 55 caracteres")
    String categoria,

    @NotNull(message = "O valor da despesa precisa ser preenchido.")
    @Positive(message = "O valor deve ser positivo.")
    Double valor,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    String descricao,

    @NotNull(message = "O número de parcelas precisa ser preenchido.")
    @Min(value = 1, message = "O parcelamento deve ser no mínimo 1.")
    Integer parcelamento,

    @NotNull(message = "A data de início deve ser preenchida.")
    @PastOrPresent(message = "A data de início não pode estar no futuro.")
    LocalDate dataInicio
    
) {  
}
