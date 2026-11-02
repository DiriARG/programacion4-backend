package com.ironempire.dto.response.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioActivoResponse {

    private Long id;
    private String nombre;
    private String grupoMuscular;
}
