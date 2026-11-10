package com.ironempire.config;

import com.ironempire.model.Ejercicio;
import com.ironempire.repository.JpaEjercicioRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class InicializacionCatalogoEjercicios implements CommandLineRunner {

    private final JpaEjercicioRepository ejercicioRepository;
    private final JsonMapper jsonMapper;

    @Override
    @Transactional
    public void run(String... args) {

        if (ejercicioRepository.count() > 0) {
            log.info("El catálogo de ejercicios ya contiene registros. Se omite la precarga.");
            return;
        }

        Ejercicio[] ejercicios;

        // Lee el archivo JSON ubicado en src/main/resources.
        try (InputStream archivo = new ClassPathResource("catalogo-ejercicios.json").getInputStream()) {

            // Mapea el contenido del archivo JSON directamente a un array de la entidad Ejercicio.
            ejercicios = jsonMapper.readValue(archivo, Ejercicio[].class);

        } catch (IOException | JacksonException exception) {
            throw new IllegalStateException(
                    "No se pudo leer el catálogo inicial de ejercicios.",
                    exception);
        }

        if (ejercicios.length == 0) {
            throw new IllegalStateException(
                    "El catálogo inicial de ejercicios está vacío.");
        }

        // Guarda todos los ejercicios dentro de la transacción.
        ejercicioRepository.saveAll(Arrays.asList(ejercicios));

        log.info("Se precargaron {} ejercicios en el catálogo.", ejercicios.length);
    }
}