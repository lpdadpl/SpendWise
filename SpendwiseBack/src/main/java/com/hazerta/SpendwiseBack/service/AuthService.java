// src/main/java/com/hazerta/spendwiseback/service/AuthService.java

package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.repository.UserRepository;
import com.hazerta.SpendwiseBack.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Mét0do para manejar el registro de un nuevo usuario
    public User registerNewUser(User user) {

        // 1. Verificación (Manejo de excepciones: Nombre de usuario o email ya existe)
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username ya está en uso."); // Usar excepciones personalizadas aquí
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya está registrado.");
        }

        // 2. Hashear la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Guardar en la DB
        return userRepository.save(user);
    }

    // La lógica de login se manejará en el Controller usando el AuthenticationManager
}