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

    @Transactional
    public UsuarioResponse desactivarUsuario(Long id, Rol rolEsperado, String nombreRecurso) {
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

    @Transactional
    public UsuarioResponse reactivarUsuario(Long id, Rol rolEsperado, String nombreRecurso) {
        Usuario usuario = validarUsuarioService.validarUsuario(id, rolEsperado, nombreRecurso);

        if (usuario.getActivo()) {
            throw new RecursoInvalidoException("El " + nombreRecurso + " ya se encuentra activo.");
        }

        usuario.setActivo(true);

        Usuario usuarioReactivado = usuarioRepository.save(usuario);

        return usuarioMapper.convertirAResponse(usuarioReactivado);
    }
}