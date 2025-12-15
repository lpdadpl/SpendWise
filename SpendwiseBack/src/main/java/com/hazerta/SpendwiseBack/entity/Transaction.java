package com.hazerta.SpendwiseBack.entity;

import com.hazerta.SpendwiseBack.model.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal; // Importante para cálculos monetarios

@Entity
@Table(name = "app_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description; // varchar

    // Tipo de dato NUMERIC/DECIMAL para precisión financiera
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // Uso de Enum para limitar los valores (INCOME, EXPENSE)
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    // Relación Many-to-One con User (user_id en la BD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación Many-to-One con Category (category_id en la BD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}