package com.hazerta.SpendwiseBack.dto;

import com.hazerta.SpendwiseBack.model.TransactionKind;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTransactionDto(
                String description,
                @NotNull(message = "El importe es obligatorio") @Positive(message = "El importe debe ser positivo") BigDecimal amount,
                @NotNull(message = "El tipo de movimiento es obligatorio") TransactionKind transactionKind,
                @NotNull(message = "La categoría es obligatoria") Long transactionTypeId) {
}
