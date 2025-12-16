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

        // Buscar usuario en la base de datos por Username O Email
        com.hazerta.SpendwiseBack.entity.User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuario no encontrado con username/email: " + username));

        // Construir y retornar el objeto UserDetails de Spring Security
        // Se asume que user.getPassword() ya tiene el hash BCrypt
        return new User(user.getUsername(),
                user.getPassword(),
                new ArrayList<>()); // Aquí se podrían añadir roles/autoridades si fuera necesario
    }
}