package com.ironempire.dto.request.turno;

import com.ironempire.enums.DiaSemana;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearTurnoRequest {

    @NotNull(message = "El profesor es obligatorio")
    private Long profesorId;

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 100, message = "El nombre de la clase no puede superar los 100 caracteres")
    private String nombreClase;

    @NotNull(message = "El día de la semana es obligatorio")
    private DiaSemana diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;
}