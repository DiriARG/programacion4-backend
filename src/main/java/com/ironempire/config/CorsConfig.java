package com.ironempire.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        /*
         * "OPTIONS" es necesario para las solicitudes "pre-flight" que el navegador
         * realiza automáticamente antes de un POST, PUT o DELETE.
         */
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // "Accept" indica qué formato de respuesta espera recibir el cliente.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

        // Tiempo de caché para las peticiones pre-flight.
        configuration.setMaxAge(3600L);

        // Aplica esta configuración de CORS a todas las rutas de la aplicación.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}