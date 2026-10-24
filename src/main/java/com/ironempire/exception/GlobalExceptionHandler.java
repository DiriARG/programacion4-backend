package com.ironempire.exception;

import com.ironempire.dto.response.error.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
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

        /*
         * Intercepta las excepciones lanzadas cuando falla la anotación @Valid en los
         * controladores.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarErrorValidacion(
                        MethodArgumentNotValidException exception) {

                /*
                 * "LinkedHashMap" mantiene el orden en el que se procesan las validaciones
                 * fallidas.
                 */
                Map<String, String> errores = new LinkedHashMap<>();

                /*
                 * Obtiene los errores de validación y, por cada campo con error,
                 * guarda en el Map (errores) el nombre del campo y su mensaje de error.
                 */
                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errores.put(error.getField(), obtenerMensajeValidacion(error)));

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

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> manejarCredencialesInvalidas(
                        BadCredentialsException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                "Las credenciales ingresadas no son válidas.",
                                null);

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(response);
        }

        @ExceptionHandler(DisabledException.class)
        public ResponseEntity<ErrorResponse> manejarUsuarioDeshabilitado(
                        DisabledException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                "Las credenciales ingresadas no son válidas.",
                                null);

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(response);
        }

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(
                        RecursoNoEncontradoException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                exception.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }

        @ExceptionHandler(RecursoInvalidoException.class)
        public ResponseEntity<ErrorResponse> manejarRecursoInvalido(
                        RecursoInvalidoException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                exception.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        /*
         * Captura errores de sintaxis y formato en el JSON (ej. valores de Enum
         * inválidos o tipos de datos incorrectos).
         */
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> manejarMensajeNoLegible(
                        HttpMessageNotReadableException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "El cuerpo de la petición contiene datos con formato o valores inválidos.",
                                null);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        /*
         * Error inesperado al interactuar con el almacenamiento; se responde con HTTP
         * 500.
         */
        @ExceptionHandler(ErrorAlmacenamientoException.class)
        public ResponseEntity<ErrorResponse> manejarErrorAlmacenamiento(
                        ErrorAlmacenamientoException exception) {

                log.error("Error al operar con el almacenamiento de archivos.", exception);

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "Ocurrió un error al procesar el archivo.",
                                null);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }

        @ExceptionHandler({
                        ArchivoDemasiadoGrandeException.class,
                        MaxUploadSizeExceededException.class
        })
        public ResponseEntity<ErrorResponse> manejarArchivoDemasiadoGrande(
                        Exception exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONTENT_TOO_LARGE.value(),
                                HttpStatus.CONTENT_TOO_LARGE.getReasonPhrase(),
                                "El archivo supera el tamaño máximo permitido.",
                                null);

                return ResponseEntity
                                .status(HttpStatus.CONTENT_TOO_LARGE)
                                .body(response);
        }

        @ExceptionHandler(TipoArchivoNoSoportadoException.class)
        public ResponseEntity<ErrorResponse> manejarTipoArchivoNoSoportado(
                        TipoArchivoNoSoportadoException exception) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                                HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(),
                                exception.getMessage(),
                                null);

                return ResponseEntity
                                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                                .body(response);
        }

        /*
         * Genera un mensaje comprensible cuando el valor ingresado para un campo no
         * puede interpretarse como el tipo de dato esperado.
         *
         * Por ejemplo, si se ingresa "abc" en page, una fecha con formato incorrecto o
         * un estado que no existe en el enum, devuelve un mensaje específico para ese
         * campo en lugar del mensaje técnico generado por Spring.
         *
         * Si el problema corresponde a una validación del DTO, como superar el tamaño
         * máximo permitido, conserva el mensaje definido en la anotación.
         */
        private String obtenerMensajeValidacion(FieldError error) {

                // Comprueba si el valor ingresado no pudo convertirse al tipo de dato esperado.
                if (error.isBindingFailure()) {
                        return switch (error.getField()) {
                                case "estado" ->
                                        "El estado debe ser VIGENTE o VENCIDA.";

                                case "fechaVencimientoDesde", "fechaVencimientoHasta" ->
                                        "La fecha debe tener el formato AAAA-MM-DD.";

                                case "page" ->
                                        "El parámetro page debe ser un número entero.";

                                case "size" ->
                                        "El parámetro size debe ser un número entero.";

                                default ->
                                        "El valor ingresado para el campo no tiene un formato válido.";
                        };
                }

                return error.getDefaultMessage();
        }

}
