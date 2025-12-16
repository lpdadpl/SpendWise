package com.hazerta.SpendwiseBack.dto;

import com.hazerta.SpendwiseBack.model.TransactionType;
import java.math.BigDecimal;

public record CreateTransactionDto(
        String description,
        BigDecimal amount,
        TransactionType transactionType,
        Long categoryId) {
}
