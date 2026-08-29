package com.ironempire.dto.request.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    /*
     * ^ (Inicio): Indica que la validación debe empezar obligatoriamente desde el
     * primer carácter del texto. No permite que haya espacios ni otros caracteres
     * antes.
     * [0-9] (Dígitos): Solo números del 0 al 9. Bloquea letras, puntos, guiones o
     * espacios; cabe aclarar que todo lo que está dentro de los corchetes "[...]"
     * es el conjunto de caracteres permitidos, osea lo que el sistema acepta.
     * {7,8} (Longitud): Mínimo 7 (documentos antiguos) y máximo 8 dígitos
     * (documentos nuevos).
     * $ (Fin): Asegura que después de los 7 u 8 números no haya nada más.
     */
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe contener entre 7 y 8 números")
    private String dni;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    /*
     * El sistema acepta:
     * 0-9: Números del 0 al 9.
     * +: El signo más (ej. +54).
     * \\s: Espacios en blanco (para separar prefijos).
     * -: Guiones (para separar bloques de números).
     */
    @Pattern(regexp = "^[0-9+\\s-]{6,20}$", message = "El formato del teléfono no es válido")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    private String contrasenia;

}