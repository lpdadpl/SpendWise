package com.hazerta.SpendwiseBack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateForecastTemplateDto(
        @NotBlank(message = "El nombre de la plantilla es obligatorio") String name,
        String description,
        @NotBlank(message = "La estructura de la plantilla es obligatoria") String structure,
        @NotNull(message = "Debe indicarse si la plantilla es pública") Boolean isPublic) {
}
