package com.github.progirls.despesas.api.despesas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRedefinirSenhaPorOtpDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String codigo,

        @NotBlank
        String novaSenha
) {
}
