package com.ironempire.service.ejercicio;

import com.ironempire.dto.request.ejercicio.ModificarEjercicioRequest;
import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
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
public class ModificarEjercicioService {

    private final JpaEjercicioRepository ejercicioRepository;
    private final ArchivoStorageService archivoStorageService;
    private final EjercicioMapper ejercicioMapper;

    private static final Set<String> TIPOS_IMAGEN_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png");

    @Transactional
    public EjercicioResponse modificarEjercicio(Long ejercicioId, ModificarEjercicioRequest request) {

        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un ejercicio con el ID indicado."));

        String nombre = request.getNombre().trim();

        if (ejercicioRepository.existsByNombreIgnoreCaseAndIdNot(nombre, ejercicioId)) {
            throw new RecursoExistenteException("Ya existe otro ejercicio registrado con ese nombre.");
        }

        String grupoMuscular = request.getGrupoMuscular().trim();

        String descripcion = request.getDescripcion();

        if (descripcion != null) {
            descripcion = descripcion.trim();

            if (descripcion.isEmpty()) {
                descripcion = null;
            }
        }

        MultipartFile imagen = request.getImagen();

        /*
         * Con estas dos variables se identifican los tres escenarios posibles de
         * comportamiento de la imagen al modificar un ejercicio:
         * 1º escenario: conservar la imagen --> tieneImagenNueva = false y
         * eliminarImagen = false
         * 2º escenario: reemplazar la imagen --> tieneImagenNueva = true y
         * eliminarImagen = false
         * 3º escenario: eliminar la imagen --> tieneImagenNueva = false y
         * eliminarImagen = true
         */
        boolean tieneImagenNueva = imagen != null && !imagen.isEmpty();
        boolean eliminarImagen = request.isEliminarImagen();

        if (tieneImagenNueva && eliminarImagen) {
            throw new RecursoInvalidoException(
                    "No se puede enviar una imagen nueva y solicitar su eliminación al mismo tiempo.");
        }

        String imagenAnterior = ejercicio.getImagenKeyMinio();

        if (tieneImagenNueva) {

            String imagenNueva = archivoStorageService.subirArchivo(
                    imagen,
                    "ejercicios",
                    TIPOS_IMAGEN_PERMITIDOS);

            programarLimpiezaAlFinalizarTransaccion(
                    imagenNueva,
                    imagenAnterior);

            ejercicio.setImagenKeyMinio(imagenNueva);

        } else if (eliminarImagen) {

            // Se elimina la referencia a la imagen para que el ejercicio se queden sin imagne.
            ejercicio.setImagenKeyMinio(null);

            // Solo se programa la eliminación cuando existe una imagen anterior almacenada.
            if (imagenAnterior != null && !imagenAnterior.isBlank()) {
                programarLimpiezaAlFinalizarTransaccion(
                        null,
                        imagenAnterior);
            }
        }

        ejercicio.setNombre(nombre);
        ejercicio.setGrupoMuscular(grupoMuscular);
        ejercicio.setDescripcion(descripcion);

        Ejercicio ejercicioModificado = ejercicioRepository.save(ejercicio);

        return ejercicioMapper.convertirAResponse(ejercicioModificado);
    }

    private void programarLimpiezaAlFinalizarTransaccion(
            String imagenNueva,
            String imagenAnterior) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    // Una vez confirmada la transacción, se elimina la imagen anterior.
                    @Override
                    public void afterCommit() {

                        if (imagenAnterior != null && !imagenAnterior.isBlank()) {
                            eliminarImagenSinInterrumpir(imagenAnterior);
                        }
                    }

                    // Si la transacción no se confirma, se elimina la imagen nueva (si se había subido) y se conserva la anterior.
                    @Override
                    public void afterCompletion(int estado) {

                        if (estado != TransactionSynchronization.STATUS_COMMITTED
                                && imagenNueva != null) {

                            eliminarImagenSinInterrumpir(imagenNueva);
                        }
                    }
                });
    }

    private void eliminarImagenSinInterrumpir(String imagenKeyMinio) {

        try {
            archivoStorageService.eliminarArchivo(imagenKeyMinio);

        } catch (RuntimeException exception) {
            log.error(
                    "No se pudo limpiar la imagen '{}' en Silo.",
                    imagenKeyMinio,
                    exception);
        }
    }
}