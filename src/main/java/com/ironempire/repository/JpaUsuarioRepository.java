package com.ironempire.repository;

import com.ironempire.enums.Rol;
import com.ironempire.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository proporciona operaciones básicas para trabajar con la entidad como findById(), findAll(), deleteById(), etc.
public interface JpaUsuarioRepository extends JpaRepository<Usuario, Long> {
    /*
     * Acá se puede agregar métodos de busquedas personalizados.
     * La primera parte es la palabra clave: findBy, existsBy, countBy, deleteBy.
     * La segunda parte indica el campo de la entidad por el cual se realiza la
     * búsqueda y debe coincidir con el nombre de la variable, comenzando con
     * mayúscula.
     * Ejemplo: findByEmail("diri@gmail.com") busca un Usuario cuyo campo
     * "email" coincida con el valor recibido, que es un String.
     * Optional<Usuario> indica que puede existir un Usuario como resultado
     * de la búsqueda, o puede no existir.
     */
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByRolAndActivoTrue(Rol rol);
}
