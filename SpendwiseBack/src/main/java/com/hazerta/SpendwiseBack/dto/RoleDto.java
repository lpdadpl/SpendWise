package com.hazerta.SpendwiseBack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleDto (

        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        String nameString

){}
