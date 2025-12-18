package com.hazerta.SpendwiseBack.dto;

import com.hazerta.SpendwiseBack.model.TransactionKind;
import java.math.BigDecimal;

public record TransactionDto(
                Long id,
                String description,
                BigDecimal amount,
                TransactionKind transactionKind,
                Long transactionTypeId,
                String transactionTypeName) {
}
