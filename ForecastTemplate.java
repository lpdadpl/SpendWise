package com.hazerta.SpendwiseBack.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "forecast_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Cambiado a Long

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Columna FK en la tabla 'category'
    private User user; // El usuario dueño de esta categoría

    @Column(nullable = false, length = 50)
    private boolean is_public;

    @Column(nullable = false, length = 50)
    private String structure;

    @Column(nullable=false)
    private LocalDate created_at;

    @Column(nullable=false)
    private LocalDate edited_at;
}
