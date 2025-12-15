// src/main/java/com/hazerta.spendwiseback/dto/RegisterDto.java

package com.hazerta.SpendwiseBack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(
        @NotBlank(message = "El nombre de usuario no puede estar vacío.")
        String username,

        @NotBlank(message = "La contraseña es requerida.")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
        String password,

        @NotBlank(message = "El email es requerido.")
        @Email(message = "Formato de email inválido.")
        String email
) {}