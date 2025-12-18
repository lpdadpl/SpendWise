package com.hazerta.SpendwiseBack.dto;

import com.hazerta.SpendwiseBack.model.TransactionKind;

public record TransactionTypeDto(Long id, String name, TransactionKind kind) {
}
