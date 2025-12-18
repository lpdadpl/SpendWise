package com.hazerta.SpendwiseBack.dto;

import java.time.LocalDateTime;

public record ForecastTemplateDto(
        Long id,
        String name,
        String description,
        Boolean isPublic,
        LocalDateTime createdAt) {
}
