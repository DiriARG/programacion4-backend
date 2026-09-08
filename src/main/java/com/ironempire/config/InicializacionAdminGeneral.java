package com.ironempire.config;

import com.ironempire.enums.Rol;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// @Slf4j es una anotación de Lombok que genera automáticamente un logger para esta clase.
@Slf4j
@Component
@RequiredArgsConstructor
/*
 * CommandLineRunner garantiza que el método "run" se ejecute automáticamente
 * apenas Spring Boot termina de levantar el contexto, antes de aceptar
 * peticiones HTTP.
 */
public class InicializacionAdminGeneral implements CommandLineRunner {

    private final JpaUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /*
     * @Value toma el valor de "application.properties" y lo inyecta a la variable
     * de Java.
     */
    @Value("${admin.general.nombre}")
    private String nombre;

    @Value("${admin.general.apellido}")
    private String apellido;

    @Value("${admin.general.dni}")
    private String dni;

    @Value("${admin.general.email}")
    private String email;

    @Value("${admin.general.telefono}")
    private String telefono;

    @Value("${admin.general.contrasenia}")
    private String contrasenia;

    /*
     * @Override indica que este método sobrescribe/implementa el método
     * run() definido por CommandLineRunner. El compilador verifica que
     * la firma del método coincida con la de la interfaz.
     */
    @Override
    @Transactional
    public void run(String... args) {

        // Se verifica si ya existe al menos un ADMIN_GENERAL habilitado en el sistema.
        if (!usuarioRepository.existsByRolAndActivoTrue(Rol.ADMIN_GENERAL)) {

            if (usuarioRepository.existsByEmail(email)) {
                /*
                 * Se utiliza "IllegalStateException" porque el conflicto se produce durante la
                 * inicialización del sistema y no durante una operación HTTP de la API.
                 */
                throw new IllegalStateException(
                        "No se pudo crear el ADMIN_GENERAL inicial porque el email configurado ya existe.");
            }

            if (usuarioRepository.existsByDni(dni)) {
                throw new IllegalStateException(
                        "No se pudo crear el ADMIN_GENERAL inicial porque el DNI configurado ya existe.");
            }

            Usuario admin = new Usuario();
            admin.setNombre(nombre);
            admin.setApellido(apellido);
            admin.setDni(dni);
            admin.setEmail(email);
            admin.setTelefono(telefono);
            admin.setPasswordHash(passwordEncoder.encode(contrasenia));
            admin.setActivo(true);
            admin.setRol(Rol.ADMIN_GENERAL);

            usuarioRepository.save(admin);

            log.info("ADMIN_GENERAL inicial creado en el sistema.");
        }
    }
}