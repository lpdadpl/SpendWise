package com.hazerta.SpendwiseBack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        // Esta respuesta solo se devolverá si el JwtRequestFilter valida un token.
        return ResponseEntity.ok(" Autenticación JWT exitosa.");
    }
}