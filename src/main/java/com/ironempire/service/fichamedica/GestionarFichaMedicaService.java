
package com.ironempire.service.fichamedica;

import com.ironempire.dto.request.fichamedica.ActualizarFichaMedicaRequest;
import com.ironempire.dto.request.fichamedica.RegistrarFichaMedicaRequest;
import com.ironempire.dto.response.fichamedica.FichaMedicaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.FichaMedicaMapper;
import com.ironempire.model.FichaMedica;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaFichaMedicaRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;
import com.ironempire.storage.ArchivoStorageService;
import com.ironempire.storage.MinioStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class GestionarFichaMedicaService {

    private final JpaFichaMedicaRepository fichaMedicaRepository;
    private final ValidarUsuarioService validarUsuarioService;
    private final ArchivoStorageService archivoStorageService;
    private final FichaMedicaMapper fichaMedicaMapper;

    // Formatos admitidos para los certificados médicos.
    private static final Set<String> TIPOS_CERTIFICADO_PERMITIDOS = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png");

    // Registra la ficha médica inicial de un alumno.
    @Transactional
    public FichaMedicaResponse registrarFichaMedica(Long alumnoId, RegistrarFichaMedicaRequest request) {

        Usuario alumno = validarUsuarioService.validarUsuario(alumnoId, Rol.ALUMNO, "alumno");

        if (fichaMedicaRepository.findByAlumnoId(alumnoId).isPresent()) {
            throw new RecursoExistenteException("El alumno ya posee una ficha médica registrada.");
        }

        MultipartFile certificado = request.getCertificado();

        String certificadoKeyMinio = archivoStorageService.subirArchivo(certificado, "fichas-medicas/" + alumnoId,
                TIPOS_CERTIFICADO_PERMITIDOS);

        // Si MySQL revierte la transacción, se intenta eliminar el nuevo objeto.
        programarLimpiezaAlFinalizarTransaccion(certificadoKeyMinio, null);

        FichaMedica fichaMedica = new FichaMedica();
        fichaMedica.setAlumno(alumno);
        fichaMedica.setCertificadoKeyMinio(certificadoKeyMinio);
        fichaMedica.setObservaciones(request.getObservaciones());
        fichaMedica.setFechaActualizacion(LocalDateTime.now());
        fichaMedica.setFechaVencimiento(request.getFechaVencimiento());

        FichaMedica fichaGuardada = fichaMedicaRepository.save(fichaMedica);

        // Devuelve los datos de la ficha sin exponer su clave interna de Silo.
        return fichaMedicaMapper.convertirAResponse(fichaGuardada);
    }

    // Renueva el certificado de una ficha médica existente.
    @Transactional
    public FichaMedicaResponse actualizarFichaMedica(Long alumnoId, ActualizarFichaMedicaRequest request) {

        validarUsuarioService.validarUsuario(alumnoId, Rol.ALUMNO, "alumno");

        FichaMedica fichaMedica = fichaMedicaRepository.findByAlumnoId(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El alumno no posee una ficha médica registrada."));

        String certificadoAnterior = fichaMedica.getCertificadoKeyMinio();

        // Almacena el nuevo certificado sin sobrescribir el objeto anterior.
        MultipartFile certificado = request.getCertificado();

        String certificadoNuevo = archivoStorageService.subirArchivo(certificado, "fichas-medicas/" + alumnoId,
                TIPOS_CERTIFICADO_PERMITIDOS);

        /*
         * Si la transacción se confirma, se elimina el certificado anterior.
         * Si se revierte, se intenta eliminar el certificado nuevo.
         */
        programarLimpiezaAlFinalizarTransaccion(
                certificadoNuevo,
                certificadoAnterior);

        // Actualiza la ficha existente con los datos nuevos.
        fichaMedica.setCertificadoKeyMinio(certificadoNuevo);
        fichaMedica.setObservaciones(request.getObservaciones());
        fichaMedica.setFechaActualizacion(LocalDateTime.now());
        fichaMedica.setFechaVencimiento(request.getFechaVencimiento());

        FichaMedica fichaActualizada = fichaMedicaRepository.save(fichaMedica);

        return fichaMedicaMapper.convertirAResponse(fichaActualizada);
    }

    /**
     * Programa la limpieza del almacenamiento para compensar un posible fallo
     * entre la operación sobre Silo y la transacción de MySQL.
     */
    private void programarLimpiezaAlFinalizarTransaccion(
            String certificadoNuevo,
            String certificadoAnterior) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        if (certificadoAnterior != null) {
                            eliminarArchivoSinInterrumpir(certificadoAnterior);
                        }
                    }

                    @Override
                    public void afterCompletion(int estado) {
                        if (estado != TransactionSynchronization.STATUS_COMMITTED) {
                            eliminarArchivoSinInterrumpir(certificadoNuevo);
                        }
                    }
                });
    }

    private void eliminarArchivoSinInterrumpir(String nombreObjeto) {

        try {
            archivoStorageService.eliminarArchivo(nombreObjeto);

        } catch (RuntimeException exception) {
            log.error(
                    "No se pudo limpiar el objeto '{}' en Silo.",
                    nombreObjeto,
                    exception);
        }
    }
}
