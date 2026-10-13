package com.ironempire.dto.request.asistencia;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarAsistenciaRequest {

    @NotNull(message = "El alumno es obligatorio")
    private Long alumnoId;

    private Long turnoId;

    @NotNull(message = "La fecha de la asistencia es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de la asistencia es obligatoria")
    private LocalTime hora;
}