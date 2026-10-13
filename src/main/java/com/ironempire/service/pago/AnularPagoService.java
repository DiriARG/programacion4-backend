package com.ironempire.service.pago;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.dto.request.pago.AnularPagoRequest;
import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.exception.RecursoExistenteException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.model.Pago;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnularPagoService {

    private final JpaPagoRepository jpaPagoRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final PagoMapper pagoMapper;
    private final CalendarioCuotas calendario;

    @Transactional
    public PagoResponse anularPago(Long pagoId, AnularPagoRequest request, String emailAdmin) {

        Pago pago = jpaPagoRepository.findConDetalleById(pagoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el pago indicado"));

        if (pago.isAnulado()) {
            throw new RecursoExistenteException("El pago ya se encuentra anulado.");
        }

        Usuario admin = jpaUsuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el perfil del usuario autenticado."));

        // Anulación lógica: el registro se conserva (RN-25).
        pago.setAnulado(true);
        pago.setFechaAnulacion(calendario.ahora());
        pago.setMotivoAnulacion(request.getMotivo().trim());
        pago.setAnuladoPor(admin);

        return pagoMapper.convertirAResponse(jpaPagoRepository.save(pago));
    }
}
