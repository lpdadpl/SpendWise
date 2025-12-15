// src/main/java/com.hazerta.spendwiseback/dto/AuthResponseDto.java

package com.hazerta.SpendwiseBack.dto;

public record AuthResponseDto(
        String accessToken,
        String tokenType
) {
    // Constructor auxiliar que establece el tokenType por defecto a "Bearer"
    public AuthResponseDto(String accessToken) {
        this(accessToken, "Bearer");
    }
}