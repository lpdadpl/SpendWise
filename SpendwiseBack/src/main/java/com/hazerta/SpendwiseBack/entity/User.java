package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.hazerta.SpendwiseBack.entity.Role;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; //hash de la contraseña

    @Column(unique = true, nullable = false)
    private String email; //

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum como string en la DB
    @Column(name = "role", nullable = false)
    private Role role = Role.USER; // Asigna el rol por defecto (USER)

}