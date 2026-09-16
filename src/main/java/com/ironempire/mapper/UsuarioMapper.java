package com.ironempire.mapper;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.model.Usuario;

import org.springframework.stereotype.Component;

@Component
/* El "mapper" convierte la entidad Usuario en el DTO UsuarioResponse.
De esta manera se evita código duplicado en los diferentes servicios. */
public class UsuarioMapper {

    public UsuarioResponse convertirAResponse(Usuario usuario) {

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