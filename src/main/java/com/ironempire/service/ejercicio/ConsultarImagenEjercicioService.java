package com.ironempire.service.ejercicio;

import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.model.Ejercicio;
import com.ironempire.repository.JpaEjercicioRepository;
import com.ironempire.storage.ArchivoDescargado;
import com.ironempire.storage.ArchivoStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarImagenEjercicioService {

    private final JpaEjercicioRepository ejercicioRepository;
    private final ArchivoStorageService archivoStorageService;

    @Transactional(readOnly = true)
    public ArchivoDescargado obtenerImagen(Long ejercicioId) {

        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un ejercicio con el ID indicado."));

        String imagenKeyMinio = ejercicio.getImagenKeyMinio();

        /*
         * Se realiza porque la imagen de un ejercicio es opcional, mientras que el
         * certificado médico es obligatorio.
         */
        if (imagenKeyMinio == null || imagenKeyMinio.isBlank()) {
            throw new RecursoNoEncontradoException(
                    "El ejercicio no tiene una imagen registrada.");
        }

        return archivoStorageService.descargarArchivo(imagenKeyMinio);
    }
}