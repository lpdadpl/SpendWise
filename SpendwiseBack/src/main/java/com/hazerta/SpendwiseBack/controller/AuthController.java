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

        // 1. Autentica las credenciales
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.username(),
                        loginDto.password()));

        // 2. Establece la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Extrae UserDetails del objeto Authentication
        // El principal del objeto Authentication es el UserDetails cargado por JwtUserDetailsService.
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Genera el token JWT
        String token = jwtUtil.generateToken(userDetails); // Ahora recibe el tipo correcto
        // --- FIN DE LA CORRECCIÓN ---

        // 5. Retorna el token al cliente
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}