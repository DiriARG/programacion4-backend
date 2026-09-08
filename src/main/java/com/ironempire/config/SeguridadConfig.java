package com.ironempire.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Acá se define a BCrypt como PasswordEncoder.
        return new BCryptPasswordEncoder();
    }

    /*
     * Expone el AuthenticationManager como un Bean para que podamos inyectarlo
     * y utilizarlo en nuestro LoginService al momento de validar las credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Se desactiva "CSRF" porque la API está protegida mediante Tokens (JWT) y no
                // mediante Cookies.
                .csrf(csrf -> csrf.disable())

                // Configura la API como "Stateless" (sin estado) para que el servidor no guarde
                // sesiones en memoria.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de acceso a las rutas.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

}
