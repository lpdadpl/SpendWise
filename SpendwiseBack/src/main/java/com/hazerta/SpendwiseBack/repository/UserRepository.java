package com.hazerta.SpendwiseBack.repository;

import com.hazerta.SpendwiseBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * CLAVE para Spring Security: Permite cargar los detalles del usuario al
     * iniciar sesión.
     */
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    // Opcional, si deseas que el registro sea por email:
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
