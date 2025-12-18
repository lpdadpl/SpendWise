package com.hazerta.SpendwiseBack.dto;

import com.hazerta.SpendwiseBack.model.TransactionKind;
import java.math.BigDecimal;

public record CreateTransactionDto(
                String description,
                BigDecimal amount,
                TransactionKind transactionKind,
                Long transactionTypeId) {
}
