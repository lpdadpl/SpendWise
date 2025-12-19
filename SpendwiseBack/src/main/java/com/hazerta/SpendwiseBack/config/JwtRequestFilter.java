package com.hazerta.SpendwiseBack.config;

import com.hazerta.SpendwiseBack.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        // DEBUG: Imprimir la URL y el header
        System.out.println("Procesando petición: " + request.getRequestURI());
        System.out.println("Cabecera de autorización: " + requestTokenHeader);

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("Usuario extraído del token: " + username);
            } catch (IllegalArgumentException e) {
                System.out.println("No se puede obtener el token JWT");
            } catch (ExpiredJwtException e) {
                System.out.println("El token JWT ha expirado");
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                System.out.println("Token JWT mal formado: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error genérico de JWT: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            if (!request.getRequestURI().startsWith("/auth/")) {
                logger.warn("El token JWT no comienza con la cadena Bearer o falta");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("SecurityContext está vacío, intentando autenticación para: " + username);
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            boolean isValid = jwtUtil.validateToken(jwtToken, userDetails);
            System.out.println("¿Es válido el token? " + isValid);

            if (isValid) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println("Autenticación establecida con éxito en SecurityContext para: " + username);
            } else {
                System.out.println("Fallo en la validación del token para el usuario: " + username);
            }
        } else {
            if (username != null) {
                System.out.println("Usuario encontrado pero SecurityContext ya tiene autenticación");
            }
        }

        chain.doFilter(request, response);
    }
}