package com.ironempire.security;

import com.ironempire.service.autenticacion.UsuarioDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioDetailsService usuarioDetailsService) {

        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            // Permite que la petición continúe hacía los siguientes filtros.
            FilterChain filterChain)
            throws ServletException, IOException {

        final String encabezadoAutorizacion = request.getHeader("Authorization");

        if (encabezadoAutorizacion == null
                || !encabezadoAutorizacion.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae solo el JWT, quitando el prefijo "Bearer ".
        final String token = encabezadoAutorizacion.substring(7);

        try {
            final String email = jwtService.extraerEmail(token);

            // Si se obtiene un email y todavía no hay una autenticación establecida...
            if (email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails usuarioDetails = usuarioDetailsService.loadUserByUsername(email);

                if (jwtService.esTokenValido(token, usuarioDetails)) {

                    /*
                     * Crea la autenticación con el usuario y sus permisos.
                     * No se necesitan credenciales porque la identidad ya fue validada mediante el
                     * JWT.
                     */
                    UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                            usuarioDetails,
                            null,
                            usuarioDetails.getAuthorities());

                    // Guarda la autenticación el contexto de seguridad para que Spring Security
                    // reconozca al usuario durante la petición.
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(autenticacion);
                }
            }

        } catch (JwtException | IllegalArgumentException exception) {

        }

        filterChain.doFilter(request, response);
    }
}