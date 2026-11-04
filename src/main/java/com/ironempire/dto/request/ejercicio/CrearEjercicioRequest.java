package com.ironempire.dto.request.ejercicio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearEjercicioRequest {

    @NotBlank(message = "El nombre del ejercicio es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El grupo muscular es obligatorio.")
    @Size(max = 50, message = "El grupo muscular no puede superar los 50 caracteres.")
    private String grupoMuscular;

    private String descripcion;

    private MultipartFile imagen;
}