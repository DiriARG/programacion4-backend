package com.ironempire.service.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioService {

    private final JpaUsuarioRepository usuarioRepository;

    /*
     * "readOnly" es una buena práctica en métodos de consulta, ya que indica que
     * la transacción es de solo lectura y permite aplicar optimizaciones.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse consultarAlumno(Long id) {
        return procesarConsulta(id, Rol.ALUMNO, "alumno");
    }

    @Transactional(readOnly = true)
    public UsuarioResponse consultarProfesor(Long id) {
        return procesarConsulta(id, Rol.PROFESOR, "profesor");
    }

    @Transactional(readOnly = true)
    public UsuarioResponse consultarAdminGestion(Long id) {
        return procesarConsulta(id, Rol.ADMIN_GESTION, "administrador de gestión");
    }

    @Transactional(readOnly = true)
    public UsuarioResponse consultarPerfilPropio(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el perfil del usuario autenticado."));

        return convertirAResponse(usuario);
    }

    private UsuarioResponse procesarConsulta(
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

        return convertirAResponse(usuario);
    }

    // Evita código duplicado para el caso de uso transversal CU-U-01.
    private UsuarioResponse convertirAResponse(Usuario usuario) {

        UsuarioResponse response = new UsuarioResponse();

        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setDni(usuario.getDni());
        response.setEmail(usuario.getEmail());
        response.setTelefono(usuario.getTelefono());
        response.setRol(usuario.getRol());
        response.setActivo(usuario.getActivo());

        return response;
    }
}