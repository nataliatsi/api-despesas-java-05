package com.github.progirls.despesas.api.despesas_api.dto;

import java.time.LocalDateTime;

public record UsuarioDTO(
        String nome,
        String email,
        LocalDateTime dataCriacao
) {
}
