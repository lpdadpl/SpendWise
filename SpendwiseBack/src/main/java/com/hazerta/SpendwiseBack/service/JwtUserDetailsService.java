package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    /**
     * Carga los detalles del usuario por nombre de usuario.
     * En una aplicación real, esta función consultaría la base de datos.
     *
     * @param username El nombre de usuario (email o login) a buscar.
     * @return UserDetails con el nombre de usuario, contraseña y roles.
     * @throws UsernameNotFoundException Si el usuario no existe.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // =========================================================================
        // --- LÓGICA DE SIMULACIÓN (Reemplazar con búsqueda en la DB real) ---
        // =========================================================================

        if ("user@hazerta.com".equals(username)) {
            // Usuario simulado: "user@hazerta.com" con la contraseña "password"
            // Nota: La contraseña debe ser la codificada si la DB la tiene codificada.
            // La contraseña codificada de "password" usando BCrypt es algo como:
            // "$2a$10$slYjKY03vO14A6G04iM2i.qO16oE4LzWbXy/Wk5k1Lp/V2c/V2Vf."

            // Usamos un objeto User de Spring Security
            return new User(username,
                    "$2a$10$slYjKY03vO14A6G04iM2i.qO16oE4LzWbXy/Wk5k1Lp/V2c/V2Vf.",
                    new ArrayList<>()); // Los roles/autoridades van aquí (ej: "ROLE_USER")

        } else {
            // =========================================================================
            // --- LÓGICA REAL (Usando un repositorio) ---
            // =========================================================================
            /*
            Optional<UserEntity> userOptional = userRepository.findByEmail(username);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("Usuario no encontrado con el email: " + username);
            }
            */
            // =========================================================================

            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + username);
        }
    }
}