package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GetUsuarioDto(
    String nome,
    LocalDateTime dataCriacao,
    List<GetDespesaDto> despesas
) {
}
