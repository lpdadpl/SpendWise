package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "transaction_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.hazerta.SpendwiseBack.model.TransactionKind kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "transactionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions;
}