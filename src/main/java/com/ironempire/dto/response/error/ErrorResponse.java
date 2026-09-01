package com.ironempire.dto.response.error;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Evita que incluya "erroresValidacion" en el JSON cuando su valor sea null.
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter 
@AllArgsConstructor 
@NoArgsConstructor 
// Este objeto define la forma estándar que tendrá el JSON de error cuando algo falle en la API.
public class ErrorResponse {
    private LocalDateTime fechaHora;
    private int codigoEstado;
    private String error;
    private String mensaje;
    /* Opcional: solo se completa cuando falla @Valid.
    Map es un diccionario clave-valor (simil tabla de dos columnas). */ 
    private Map<String, String> erroresValidacion;
}
