package com.ironempire.service.usuario;

import com.ironempire.dto.request.usuario.CrearUsuarioRequest;
import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearUsuarioService {

    private final JpaUsuarioRepository usuarioRepository;
    // El PasswordEncoder se utiliza para convertir la contraseña en un hash seguro.
    private final PasswordEncoder passwordEncoder;

    /*
     * "crearAlumno", "crearProfesor" y "crearAdminGestion" son métodos públicos porque pueden ser
     * utilizados desde un controlador.
     * Se utiliza "@Transactional" porque la operación
     * completa (verificar email, verificar dni, construir usuario, etc) es una
     * única operación de negocio, ademas que garantiza que sea atómica.
     * A su vez ambas utilizan la misma lógica de creación (procesarCreación), la
     * única diferencia entre ellas es el rol que se les asigna.
     */
    @Transactional
    public UsuarioResponse crearAlumno(CrearUsuarioRequest request) {
        return procesarCreacion(request, Rol.ALUMNO);
    }

    @Transactional
    public UsuarioResponse crearProfesor(CrearUsuarioRequest request) {
        return procesarCreacion(request, Rol.PROFESOR);
    }

    @Transactional
    public UsuarioResponse crearAdminGestion(CrearUsuarioRequest request) {
        return procesarCreacion(request, Rol.ADMIN_GESTION);
    }

    /*
     * Acá esta el método privado que contiene la lógica común para crear cualquier
     * tipo de usuario.
     * Recibe:
     * - request: El objeto que trae los datos escritos por el usuario.
     * - rolAsignado: rol que el backend decidió asignarle.
     * Es "private" para que ningun controller pueda invocarlo directamente. Los
     * únicos puntos de entrada son: crearAlumno(), crearProfesor() y crearAdminGestion().
     */
    private UsuarioResponse procesarCreacion(CrearUsuarioRequest request, Rol rolAsignado) {
        // Validaciones.
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RecursoExistenteException("El email ingresado ya se encuentra registrado.");
        }

        if (usuarioRepository.existsByDni(request.getDni())) {
            throw new RecursoExistenteException("El DNI ingresado ya se encuentra registrado.");
        }

        // Se crea el nuevo objeto "Usuario" copiando los datos que brindó el
        // cliente.
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setApellido(request.getApellido());
        nuevoUsuario.setDni(request.getDni());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setTelefono(request.getTelefono());
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(request.getContrasenia()));
        nuevoUsuario.setActivo(true);
        /*
         * Se asigna el rol recibido como parámetro, osea:
         * Si llega desde crearAlumno(): rolAsignado = Rol.ALUMNO
         * Si llega desde crearProfesor(): rolAsignado = Rol.PROFESOR
         * De esta manera el cliente no puede elegir libremente un rol como ADMIN.
         */
        nuevoUsuario.setRol(rolAsignado);

        // Se guarda el usuario en la bd.
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        /*
         * No se devuelve directamente la entidad Usuario, sino un UsuarioResponse,
         * evitando mostrar info comprometedora en la respuesta como
         * por ej passwordHash.
         */
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