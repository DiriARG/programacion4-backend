package com.ironempire.service.pago;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ironempire.dto.request.pago.RegistrarPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.ErrorAlmacenamientoException;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.exception.TipoArchivoNoSoportadoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.model.Pago;
import com.ironempire.model.Plan;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaPlanRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.service.notificacion.PagoRegistradoEvent;
import com.ironempire.storage.MinioStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrarPagoService {

    private final JpaPagoRepository jpaPagoRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final JpaPlanRepository jpaPlanRepository;
    private final PagoMapper pagoMapper;
    private final CalendarioCuotas calendario;
    private final MinioStorageService minioStorageService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PagoResponse registrarPago(RegistrarPagoRequest request, MultipartFile comprobante) {

        // RN-18 / E2 / E3 / E4
        Usuario alumno = jpaUsuarioRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado"));

        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RecursoInvalidoException("El usuario no es un ALUMNO");
        }
        if (!Boolean.TRUE.equals(alumno.getActivo())) {
            throw new RecursoInvalidoException("El alumno está inactivo y no puede registrar pagos.");
        }

    
        Plan plan = jpaPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el plan indicado"));

        
        if (!Boolean.TRUE.equals(plan.getActivo())) {
            throw new RecursoInvalidoException(
                    "El plan seleccionado está inactivo y no puede utilizarse para registrar pagos.");
        }

        YearMonth periodo = YearMonth.of(request.getAnioPeriodo(), request.getMesPeriodo());
        LocalDate fechaVencimiento = calendario.vencimientoDe(periodo);

        // RN-24 / E7: un pago vigente (no anulado) por alumno y período.
        if (jpaPagoRepository.existsByAlumnoIdAndFechaVencimientoAndAnuladoFalse(alumno.getId(), fechaVencimiento)) {
            throw new RecursoExistenteException("El alumno ya tiene un pago registrado para ese período.");
        }

        // Recién ahora se sube el archivo: si algo de lo anterior falla, no queda nada huérfano en MinIO.
        String keyComprobante = subirComprobante(comprobante);

        try {
            Pago pago = new Pago();
            pago.setAlumno(alumno);
            pago.setPlan(plan);
            pago.setMontoAbonado(request.getMontoAbonado());
            pago.setFechaPago(request.getFechaPago());
            pago.setFechaVencimiento(fechaVencimiento);
            pago.setMetodoPago(request.getMetodoPago());
            pago.setComprobanteKeyMinio(keyComprobante);

            Pago guardado = jpaPagoRepository.save(pago);

            // El recibo se envía DESPUÉS del commit y en otro hilo (ver EnviarReciboPagoService).
            eventPublisher.publishEvent(new PagoRegistradoEvent(guardado.getId()));

            return pagoMapper.convertirAResponse(guardado);

        } catch (RuntimeException e) {
            // E8: si falla el guardado, no debe quedar el comprobante subido.
            eliminarSilencioso(keyComprobante);
            throw e;
        }
    }

    private String subirComprobante(MultipartFile comprobante) {
        if (comprobante == null || comprobante.isEmpty()) {
            return null; // A2: pago sin comprobante (por ejemplo, en efectivo).
        }

        String tipo = comprobante.getContentType();
        boolean valido = tipo != null && (tipo.startsWith("image/") || tipo.equals("application/pdf"));
        if (!valido) {
            throw new TipoArchivoNoSoportadoException("El comprobante debe ser una imagen o un PDF.");
        }

        String key = "comprobantes/" + UUID.randomUUID() + extensionDe(tipo);

        try {
            return minioStorageService.subirArchivo(key, comprobante.getInputStream(), comprobante.getSize(), tipo);
        } catch (IOException e) {
            throw new ErrorAlmacenamientoException("No se pudo leer el comprobante.", e);
        }
    }

    private String extensionDe(String tipo) {
        return switch (tipo) {
            case "application/pdf" -> ".pdf";
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }

    private void eliminarSilencioso(String key) {
        if (key == null) {
            return;
        }
        try {
            minioStorageService.eliminarArchivo(key);
        } catch (RuntimeException ignorada) {
            // Si tampoco se puede borrar, se prioriza informar el error original.
        }
    }
}