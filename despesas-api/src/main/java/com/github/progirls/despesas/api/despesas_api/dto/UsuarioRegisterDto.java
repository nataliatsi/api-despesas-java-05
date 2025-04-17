package com.github.progirls.despesas.api.despesas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRegisterDto(
    @NotBlank(message = "O nome não pode ser vazio")
    @Size(min = 4, max = 20, message = "O nome deve ter no mínimo 4 caracteres")
    String nome,

    @NotBlank(message = "O Email é obrigatório!")
    @Email(message = "Formato do email inválido!")
    String email,
    
    @NotBlank(message = "A senha não pode ser vazia")
    @Size(min = 8, message = "A senha deve conter no mínimo 8 caracteres!")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
    String senha
    
    ) {
}