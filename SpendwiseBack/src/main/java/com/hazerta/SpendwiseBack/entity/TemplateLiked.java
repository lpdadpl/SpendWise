package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "template_liked")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateLiked {

    // En las tablas de unión M:N se usa una clave primaria compuesta,
    // pero es más sencillo usar una clave primaria artificial (ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Many-to-One con User (owner_id) - Quién dio el "Me Gusta"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relación Many-to-One con ForecastTemplate (id_template) - A qué plantilla se dio "Me Gusta"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_template", nullable = false)
    private ForecastTemplate template;
}