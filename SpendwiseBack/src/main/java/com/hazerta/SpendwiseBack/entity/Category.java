// src/main/java/com.hazerta/spendwiseback/entity/Category.java

package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Cambiado a Long

    @Column(nullable = false, length = 50)
    private String name; // Nombre de la categoría (ej: "Salario", "Alquiler")

    // Relación Many-to-One con User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Columna FK en la tabla 'category'
    private User user; // El usuario dueño de esta categoría

    // Campo para saber si es Gasto o Ingreso (usaremos un Enum más adelante)
    @Column(length = 10, nullable = false)
    private String type; // Por ahora String, luego lo cambiaremos a Enum
}