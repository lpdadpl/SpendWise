package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "forecast_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description; // varchar

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    // Relación Many-to-One con User (owner_id en la BD)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // Usuario dueño de la plantilla

    @Column(columnDefinition = "TEXT")
    private String structure;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Relación One-to-Many para "Me Gusta" (TemplateLiked)
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TemplateLiked> likedBy;
}