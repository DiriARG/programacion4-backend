package com.ironempire.service.usuario;

import com.ironempire.dto.request.usuario.ModificarUsuarioRequest;
import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModificarUsuarioService {

    private final JpaUsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse modificarAlumno(Long id, ModificarUsuarioRequest request) {
        return procesarModificacion(id, request, Rol.ALUMNO, "alumno");
    }

    @Transactional
    public UsuarioResponse modificarProfesor(Long id, ModificarUsuarioRequest request) {
        return procesarModificacion(id, request, Rol.PROFESOR, "profesor");
    }

    @Transactional
    public UsuarioResponse modificarAdminGestion(Long id, ModificarUsuarioRequest request) {
        return procesarModificacion(id, request, Rol.ADMIN_GESTION, "administrador de gestión");
    }

    private UsuarioResponse procesarModificacion(Long id, ModificarUsuarioRequest request, Rol rolEsperado,
            String nombreRecurso) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró un " + nombreRecurso
                                + " con el ID ingresado."));

        if (usuario.getRol() != rolEsperado) {
            throw new RecursoInvalidoException(
                    "El usuario indicado no es un " + nombreRecurso + ".");
        }

        /*
         * Verifica si el email pertenece a otro usuario.
         * Se excluye al usuario actual mediante su "id" para permitir conservar su propio
         * email.
         */
        if (usuarioRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new RecursoExistenteException("El email ingresado ya se encuentra registrado.");
        }

        if (usuarioRepository.existsByDniAndIdNot(request.getDni(), id)) {
            throw new RecursoExistenteException("El DNI ingresado ya se encuentra registrado.");
        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setDni(request.getDni());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuarioGuardado.getId());
        response.setNombre(usuarioGuardado.getNombre());
        response.setApellido(usuarioGuardado.getApellido());
        response.setDni(usuarioGuardado.getDni());
        response.setEmail(usuarioGuardado.getEmail());
        response.setTelefono(usuarioGuardado.getTelefono());
        response.setRol(usuarioGuardado.getRol());
        response.setActivo(usuarioGuardado.getActivo());

        return response;
    }
}