package com.ironempire.service.ejercicio;

import com.ironempire.dto.request.ejercicio.CrearEjercicioRequest;
import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.mapper.EjercicioMapper;
import com.ironempire.model.Ejercicio;
import com.ironempire.repository.JpaEjercicioRepository;
import com.ironempire.storage.ArchivoStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrearEjercicioService {

    private final JpaEjercicioRepository ejercicioRepository;
    private final ArchivoStorageService archivoStorageService;
    private final EjercicioMapper ejercicioMapper;

    private static final Set<String> TIPOS_IMAGEN_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png");

    @Transactional
    public EjercicioResponse crearEjercicio(CrearEjercicioRequest request) {

        // Se eliminan los espacios de los extremos para evitar nombres duplicados por
        // diferencias de formato.
        String nombre = request.getNombre().trim();

        if (ejercicioRepository.existsByNombreIgnoreCase(nombre)) {
            throw new RecursoExistenteException(
                    "Ya existe un ejercicio registrado con ese nombre.");
        }

        /*
         * Acá se aplica el trim para que el catálogo mantenga un formato consistente,
         * evitando que se guarde por ej:
         * Pecho
         *  Pecho
         * Pecho 
         */
        String grupoMuscular = request.getGrupoMuscular().trim();

        // La descripción también es opcional.
        String descripcion = request.getDescripcion();

        if (descripcion != null) {
            descripcion = descripcion.trim();

            if (descripcion.isEmpty()) {
                descripcion = null;
            }
        }

        String imagenKeyMinio = null;

        MultipartFile imagen = request.getImagen();

        // La imagen también es opcional: solamente se sube cuando se recibió un
        // archivo.
        if (imagen != null && !imagen.isEmpty()) {

            imagenKeyMinio = archivoStorageService.subirArchivo(
                    imagen,
                    "ejercicios",
                    TIPOS_IMAGEN_PERMITIDOS);

            // Se elimina la imagen si la transacción no termino de completarse.
            programarLimpiezaSiFallaTransaccion(imagenKeyMinio);
        }

        Ejercicio ejercicio = new Ejercicio();

        ejercicio.setNombre(nombre);
        ejercicio.setGrupoMuscular(grupoMuscular);
        ejercicio.setDescripcion(descripcion);
        ejercicio.setImagenKeyMinio(imagenKeyMinio);
        ejercicio.setActivo(true);

        Ejercicio ejercicioGuardado = ejercicioRepository.save(ejercicio);

        return ejercicioMapper.convertirAResponse(ejercicioGuardado);
    }

    private void programarLimpiezaSiFallaTransaccion(String imagenKeyMinio) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCompletion(int estado) {

                        if (estado != TransactionSynchronization.STATUS_COMMITTED) {
                            eliminarImagenSinInterrumpir(imagenKeyMinio);
                        }
                    }
                });
    }

    private void eliminarImagenSinInterrumpir(String imagenKeyMinio) {

        try {
            archivoStorageService.eliminarArchivo(imagenKeyMinio);

        } catch (RuntimeException exception) {
            log.error(
                    "No se pudo limpiar la imagen '{}' en Silo luego de fallar la transacción.",
                    imagenKeyMinio,
                    exception);
        }
    }
}