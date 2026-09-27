package com.ironempire.service.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.mapper.UsuarioMapper;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionarEstadoUsuarioService {

    private final ValidarUsuarioService validarUsuarioService;
    private final JpaUsuarioRepository usuarioRepository;
    private final JpaTurnoRepository turnoRepository;
    private final UsuarioMapper usuarioMapper;

    // Métodos públicos de desactivación.
    @Transactional
    public UsuarioResponse desactivarAlumno(Long id) {
        return procesarDesactivacion(id, Rol.ALUMNO, "alumno");
    }

    @Transactional
    public UsuarioResponse desactivarProfesor(Long id) {
        return procesarDesactivacion(id, Rol.PROFESOR, "profesor");
    }

    @Transactional
    public UsuarioResponse desactivarAdminGestion(Long id) {
        return procesarDesactivacion(id, Rol.ADMIN_GESTION, "administrador de gestión");
    }

    // Métodos públicos de reactivación.
    @Transactional
    public UsuarioResponse reactivarAlumno(Long id) {
        return procesarReactivacion(id, Rol.ALUMNO, "alumno");
    }

    @Transactional
    public UsuarioResponse reactivarProfesor(Long id) {
        return procesarReactivacion(id, Rol.PROFESOR, "profesor");
    }

    @Transactional
    public UsuarioResponse reactivarAdminGestion(Long id) {
        return procesarReactivacion(id, Rol.ADMIN_GESTION, "administrador de gestión");
    }

    // Métodos privados reutilizables.
    private UsuarioResponse procesarDesactivacion(Long id, Rol rolEsperado, String nombreRecurso) {
        Usuario usuario = validarUsuarioService.validarUsuario(id, rolEsperado, nombreRecurso);

        if (!usuario.getActivo()) {
            throw new RecursoInvalidoException("El " + nombreRecurso + " ya se encuentra inactivo.");
        }

        usuario.setActivo(false);

        /*
         * Si el usuario a desactivar es un profesor, sus turnos activos deben pasar
         * automáticamente a estado inactivo, manteniendo intactos los registros
         * históricos.
         */
        if (usuario.getRol() == Rol.PROFESOR) {
            List<Turno> turnosActivos = turnoRepository.findByProfesorIdAndActivoTrue(usuario.getId());
            /*
             * Se recorren todos los turnos contenidos en la lista turnosActivos.
             * "Turno turno" declara una variable temporal de tipo Turno que, en cada
             * iteración, representa al turno actual que se está procesando.
             */
            for (Turno turno : turnosActivos) {
                /*
                 * Se modifica el estado en memoria. Hibernate guardará los cambios en la bd al
                 * finalizar la transacción por mecanismo de "dirty checking" (verificación de
                 * cambios).
                 */
                turno.setActivo(false);
            }
        }

        Usuario usuarioDesactivado = usuarioRepository.save(usuario);
        return usuarioMapper.convertirAResponse(usuarioDesactivado);
    }

    private UsuarioResponse procesarReactivacion(Long id, Rol rolEsperado, String nombreRecurso) {
        Usuario usuario = validarUsuarioService.validarUsuario(id, rolEsperado, nombreRecurso);

        if (usuario.getActivo()) {
            throw new RecursoInvalidoException("El " + nombreRecurso + " ya se encuentra activo.");
        }

        usuario.setActivo(true);

        Usuario usuarioReactivado = usuarioRepository.save(usuario);
        return usuarioMapper.convertirAResponse(usuarioReactivado);
    }
}