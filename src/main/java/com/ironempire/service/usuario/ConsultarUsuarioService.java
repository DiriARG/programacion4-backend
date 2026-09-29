package com.ironempire.service.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.mapper.UsuarioMapper;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioService {

    private final ValidarUsuarioService validarUsuarioService;
    private final JpaUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    /*
     * Consultas individuales.
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

    // Listados.
    @Transactional(readOnly = true)
    public List<UsuarioResponse> consultarAlumnos() {
        return procesarListado(Rol.ALUMNO);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> consultarProfesores() {
        return procesarListado(Rol.PROFESOR);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> consultarAdministradoresGestion() {
        return procesarListado(Rol.ADMIN_GESTION);
    }

    private UsuarioResponse procesarConsulta(Long id, Rol rolEsperado, String nombreRecurso) {
        Usuario usuario = validarUsuarioService.validarUsuario(id, rolEsperado, nombreRecurso);

        return usuarioMapper.convertirAResponse(usuario);
    }

    private List<UsuarioResponse> procesarListado(Rol rol) {
        List<Usuario> usuarios = usuarioRepository.findByRol(rol);

        return usuarios.stream()
                .map(usuarioMapper::convertirAResponse)
                .toList();
    }

}