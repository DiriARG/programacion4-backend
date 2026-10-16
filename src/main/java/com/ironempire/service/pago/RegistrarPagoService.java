package com.ironempire.service.pago;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.model.Pago;
import com.ironempire.model.Plan;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaPlanRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;
import com.ironempire.storage.ArchivoStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrarPagoService {

    private final JpaPagoRepository pagoRepository;
    private final JpaPlanRepository planRepository;
    private final ValidarUsuarioService validarUsuarioService;
    private final ArchivoStorageService archivoStorageService;
    private final PagoMapper pagoMapper;

    private static final Set<String> TIPOS_COMPROBANTE_PERMITIDOS = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png");

    @Transactional
    public PagoResponse registrarPago(RegistrarPagoRequest request) {

        Usuario alumno = validarUsuarioService.validarUsuario(request.getAlumnoId(), Rol.ALUMNO, "alumno");

        if (!alumno.getActivo()) {
            throw new RecursoInvalidoException("No se puede registrar un pago para un alumno inactivo.");
        }

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un plan con el ID ingresado."));

        if (!plan.getActivo()) {
            throw new RecursoInvalidoException("No se puede registrar un pago con un plan inactivo.");
        }

        /*
         * Se calcula automáticamente el vencimiento: día 10 del período. ej:
         * Período recibido: 2026-10 --> se le aplica atDay(10) --> fecha de
         * vencimiento: 2026-10-10
         */
        LocalDate fechaVencimiento = request.getPeriodo().atDay(10);

        if (pagoRepository.existsByAlumnoIdAndFechaVencimiento(alumno.getId(), fechaVencimiento)) {
            throw new RecursoExistenteException("El alumno ya posee un pago registrado para el período indicado.");
        }

        // Almacena el comprobante únicamente si se adjuntó uno.
        String comprobanteKeyMinio = null;
        MultipartFile comprobante = request.getComprobante();

        if (comprobante != null && !comprobante.isEmpty()) {

            comprobanteKeyMinio = archivoStorageService.subirArchivo(
                    comprobante,
                    "comprobantes-pago/" + alumno.getId(),
                    TIPOS_COMPROBANTE_PERMITIDOS);

            programarLimpiezaSiRevierte(comprobanteKeyMinio);
        }

        Pago pago = new Pago();
        pago.setAlumno(alumno);
        pago.setPlan(plan);
        pago.setMontoAbonado(request.getMontoAbonado());
        pago.setFechaPago(request.getFechaPago());
        pago.setFechaVencimiento(fechaVencimiento);
        pago.setMetodoPago(request.getMetodoPago());
        pago.setComprobanteKeyMinio(comprobanteKeyMinio);

        /*
         * saveAndFlush fuerza el envío del INSERT a MySQL para detectar acá posibles
         * conflictos de integridad, como un pago duplicado.
         * Con save(), el INSERT podría ejecutarse recién al confirmar la transacción,
         * fuera de este try-catch.
         */
        try {
            Pago pagoGuardado = pagoRepository.saveAndFlush(pago);

            return pagoMapper.convertirAResponse(pagoGuardado);

        } catch (DataIntegrityViolationException exception) {

            throw new RecursoExistenteException(
                    "No se pudo registrar el pago porque ya existe un registro "
                            + "para el período indicado o se incumple una restricción "
                            + "de integridad.");
        }
    }

    /**
     * Si la transacción de MySQL se revierte, intenta eliminar el comprobante
     * que se había subido a Silo/MinIO.
     */
    private void programarLimpiezaSiRevierte(String nombreObjeto) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    // Se ejecuta cuando la transacción registrarPago finaliza.
                    @Override
                    public void afterCompletion(int estado) {

                        if (estado != TransactionSynchronization.STATUS_COMMITTED) {
                            eliminarArchivoSinInterrumpir(nombreObjeto);
                        }
                    }
                });
    }

    private void eliminarArchivoSinInterrumpir(String nombreObjeto) {

        try {
            archivoStorageService.eliminarArchivo(nombreObjeto);

        } catch (RuntimeException exception) {
            log.error(
                    "No se pudo limpiar el comprobante '{}' en Silo.",
                    nombreObjeto,
                    exception);
        }
    }
}