package com.ironempire.service.usuario;

import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarUsuarioService {

    private final JpaUsuarioRepository usuarioRepository;

    /*
     * Este método reemplaza el código duplicado en distintos servicios ej:
     * modificación y consulta. Busca al usuario y valida que su rol coincida con el
     * esperado.
     */
    public Usuario validarUsuario(
            Long id,
            Rol rolEsperado,
            String nombreRecurso) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un " + nombreRecurso
                                + " con el ID ingresado."));

        if (usuario.getRol() != rolEsperado) {
            throw new RecursoInvalidoException(
                    "El usuario indicado no es un " + nombreRecurso + ".");
        }

        return usuario;
    }
}