package com.ironempire.service.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioService {

    private final ValidarUsuarioService validarUsuarioService;

    /*
     * "readOnly" indica que la transacción se utiliza para realizar operaciones
     * de lectura y no para modificar datos.
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

    private UsuarioResponse procesarConsulta(
            Long id,
            Rol rolEsperado,
            String nombreRecurso) {

        Usuario usuario = validarUsuarioService.validarUsuario(id, rolEsperado, nombreRecurso);

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