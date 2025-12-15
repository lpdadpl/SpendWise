package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.config.JwtUtil;
import com.hazerta.SpendwiseBack.dto.JwtRequest;
import com.hazerta.SpendwiseBack.dto.JwtResponse;
import com.hazerta.SpendwiseBack.dto.UserDto;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.service.AuthService; // <-- IMPORTAMOS EL SERVICIO DE NEGOCIO
import com.hazerta.SpendwiseBack.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // Permite peticiones desde dominios cruzados
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;
    // ----------------------------------------------------

    /**
     * Endpoint para la autenticación de usuarios (LOGIN).
     *
     * @param authenticationRequest Contiene username y password.
     * @return ResponseEntity con el token JWT si la autenticación es exitosa.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        // 1. Intentar autenticar con el manager de Spring Security
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // 2. Si la autenticación es exitosa, cargar UserDetails
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        // 3. Generar el token JWT usando el JwtUtil
        final String token = jwtUtil.generateToken(userDetails);

        // 4. Devolver el token al cliente
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            // Este método verifica las credenciales usando el UserDetailsService y PasswordEncoder
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            // La excepción BadCredentialsException es la que se lanza cuando la contraseña no coincide.
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * Endpoint para el registro de nuevos usuarios.
     *
     * @param user Contiene username, email y password (sin hashear).
     * @return 201 Created si el registro es exitoso.
     */
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDto user) {


        User newUser = new User();
        newUser.setUsername(user.username());
        newUser.setEmail(user.email());
        newUser.setPassword(user.password()); // Password sin hashear

        try {
            // --- SOLUCIÓN 2: LLAMAR A LA INSTANCIA INYECTADA ---
            // El AuthService se encarga de hashear la contraseña y guardar el usuario.
            authService.registerNewUser(newUser);
            // ----------------------------------------------------

            return new ResponseEntity<>("Registro exitoso.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Capturamos las excepciones de negocio (ej: Username o email ya existen)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}