package com.ironempire.dto.response.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfesorDisponibleResponse {

    private Long id;
    private String nombre;
    private String apellido;
}