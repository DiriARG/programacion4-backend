package com.ironempire.dto.request.fichamedica;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarFichaMedicaRequest {

    @NotNull(message = "El certificado médico es obligatorio")
    private MultipartFile certificado;

    private String observaciones;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    // Anotación que permite fechas futuras o iguales a la fecha actual, pero rechaza a las anteriores.
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser anterior a la fecha actual.")
    private LocalDate fechaVencimiento;
}