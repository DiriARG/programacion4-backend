package com.ironempire.dto.request.fichamedica;

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
public class ActualizarFichaMedicaRequest {

    @NotNull(message = "El nuevo certificado médico es obligatorio")

    // Representa el archivo físico enviado por el clienteen la petición HTTP.
    private MultipartFile certificado;

    private String observaciones;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;
}