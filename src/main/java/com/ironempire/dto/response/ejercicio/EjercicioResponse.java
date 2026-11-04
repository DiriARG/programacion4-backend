package com.ironempire.dto.response.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioResponse {
    private Long id;
    private String nombre;
    private String grupoMuscular;
    private String descripcion;
    private String imagenUrl;
    private Boolean activo;
}
