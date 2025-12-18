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

    public User updateUser(String currentUsername, com.hazerta.SpendwiseBack.dto.UpdateUserDto updates) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updates.username() != null && !updates.username().isEmpty()) {
            if (!user.getUsername().equals(updates.username()) && userRepository.existsByUsername(updates.username())) {
                throw new RuntimeException("El nombre de usuario ya está en uso");
            }
            user.setUsername(updates.username());
        }

        if (updates.email() != null && !updates.email().isEmpty()) {
            if (!user.getEmail().equals(updates.email()) && userRepository.existsByEmail(updates.email())) {
                throw new RuntimeException("El email ya está en uso");
            }
            user.setEmail(updates.email());
        }

        if (updates.password() != null && !updates.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updates.password()));
        }

        return userRepository.save(user);
    }

    public User registerNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username ya está en uso.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya está registrado.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}