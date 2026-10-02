package com.ironempire.dto.response.turno;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoTurnoResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
}