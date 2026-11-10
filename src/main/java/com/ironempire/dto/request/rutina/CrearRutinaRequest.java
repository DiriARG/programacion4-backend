package com.ironempire.dto.request.rutina;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearRutinaRequest {

    @NotNull(message = "El alumno es obligatorio.")
    private Long alumnoId;

    @NotBlank(message = "El nombre de la rutina es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotEmpty(message = "La rutina debe contener al menos un ejercicio.")
    @Valid
    private List<RutinaEjercicioRequest> ejercicios;
}