package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.service.AuthService;
import com.hazerta.SpendwiseBack.config.JwtUtil;
import com.hazerta.SpendwiseBack.dto.LoginDto;
import com.hazerta.SpendwiseBack.dto.RegisterDto;
import com.hazerta.SpendwiseBack.dto.AuthResponseDto;
import com.hazerta.SpendwiseBack.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Importación necesaria
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

        User newUser = new User();
        newUser.setUsername(registerDto.username());
        newUser.setEmail(registerDto.email());
        newUser.setPassword(registerDto.password()); // El servicio se encarga de hashear

        try {
            authService.registerNewUser(newUser);
            return new ResponseEntity<>("Registro exitoso.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        System.out.println("Intento de login para usuario: " + loginDto.username());

        try {
            // 1. Autentica las credenciales
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.username(),
                            loginDto.password()));

            // 2. Establece la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Extrae UserDetails del objeto Authentication
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 4. Genera el token JWT
            String token = jwtUtil.generateToken(userDetails);

            System.out.println("Login exitoso para usuario: " + loginDto.username());

            // 5. Retorna el token al cliente
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);

        } catch (org.springframework.security.core.AuthenticationException e) {
            System.out.println("Fallo de autenticación para usuario " + loginDto.username() + ": " + e.getMessage());
            throw e; // Dejar que Spring Security maneje la excepción (o devolver 401 explícitamente)
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody com.hazerta.SpendwiseBack.dto.UpdateUserDto updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        try {
            authService.updateUser(currentUsername, updates);
            return new ResponseEntity<>("Perfil actualizado correctamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}