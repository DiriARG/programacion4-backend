package com.ironempire.dto.request.usuario;

import com.ironempire.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class UsuarioRequest {
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private Boolean activo;
    private Rol rol;

}
