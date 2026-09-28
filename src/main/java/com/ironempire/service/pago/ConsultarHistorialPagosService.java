package com.ironempire.service.pago;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.repository.JpaPagoRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultarHistorialPagosService {
    private final JpaPagoRepository jpaPagoRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final PagoMapper pagoMapper;
    
    @Transactional(readOnly = true)
    public List<PagoResponse> consultarHistorialPagos(String email) {
        
        Long alumnoId = jpaUsuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el perfil del usuario autenticado."))
                .getId();

        return jpaPagoRepository.findByAlumnoIdOrderByFechaVencimientoDesc(alumnoId).stream()
                .map(pagoMapper::convertirAResponse)
                .toList();

    }
}
