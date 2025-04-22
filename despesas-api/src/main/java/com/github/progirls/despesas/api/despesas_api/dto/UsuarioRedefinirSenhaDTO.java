package com.github.progirls.despesas.api.despesas_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRedefinirSenhaDTO(

        @NotBlank(message = "A senha não pode ser vazia") 
        @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres!") 
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", 
            message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial") 
            String senha) {

}
