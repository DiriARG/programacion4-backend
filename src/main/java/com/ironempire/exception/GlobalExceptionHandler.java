package com.ironempire.exception;

import com.ironempire.dto.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoExistenteException.class)
        public ResponseEntity<ErrorResponse> manejarRecursoExistente(
                        RecursoExistenteException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                exception.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(response);
        }

        // Intercepta las excepciones lanzadas cuando falla la anotación @Valid en los
        // controladores.
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarErrorValidacion(
                        MethodArgumentNotValidException exception) {

                // "LinkedHashMap" mantiene el orden en el que se procesan las validaciones
                // fallidas.
                Map<String, String> errores = new LinkedHashMap<>();

                /*
                 * Obtiene los errores de validación y, por cada campo con error,
                 * guarda en el Map (errores) el nombre del campo y su mensaje de error.
                 */
                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Error de validación",
                                errores);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }
}