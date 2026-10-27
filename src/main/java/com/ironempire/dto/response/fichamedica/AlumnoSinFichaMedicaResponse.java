package com.ironempire.dto.response.fichamedica;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoSinFichaMedicaResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
}