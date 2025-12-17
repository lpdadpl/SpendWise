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

        String username = null;
        String jwtToken = null;

        // 1. Extraer el token del encabezado 'Authorization'
        // El token usualmente viene en formato: "Bearer <token>"
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // 2. Extraer el nombre de usuario usando JwtUtil
                username = jwtUtil.extractUsername(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.warn("No se pudo obtener el token JWT");
            } catch (ExpiredJwtException e) {
                logger.warn("El token JWT ha expirado");
                // Podrías lanzar una excepción o configurar una respuesta específica aquí
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                logger.warn("Token JWT malformado: " + e.getMessage());
            }
        } else {
            // Solo loguear advertencia si NO es un endpoint público de autenticación
            if (!request.getRequestURI().startsWith("/auth/")) {
                logger.warn("El token JWT no comienza con 'Bearer String' o no está presente.");
            }
        }

        // 3. Validar el token y establecer la autenticación si no está ya autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargar los detalles del usuario desde la base de datos
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // 4. Si el token es válido
            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                // Crea el objeto de autenticación
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Añade detalles de la solicitud (IP, sesión, etc.)
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Configurar el usuario en el Contexto de Seguridad
                // Esto indica a Spring Security que el usuario actual está autenticado
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continuar con el siguiente filtro en la cadena o el servlet de destino
        chain.doFilter(request, response);
    }
}