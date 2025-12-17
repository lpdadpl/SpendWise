package com.hazerta.SpendwiseBack.dto;

public record UpdateUserDto(
        String username,
        String email,
        String password) {
}
