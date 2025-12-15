package com.hazerta.SpendwiseBack.dto;

import java.io.Serializable;

// Se utiliza para la petición de login: { "username": "...", "password": "..." }
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    // Se necesita un constructor por defecto para la deserialización de JSON
    public JwtRequest() {}

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}