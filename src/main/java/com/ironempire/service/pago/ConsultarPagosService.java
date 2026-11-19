package com.ironempire.service.pago;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.dto.response.pago.PagoResponse;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.mapper.PagoMapper;
import com.ironempire.repository.JpaPagoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultarPagosService {

    private static final LocalDate FECHA_MINIMA = LocalDate.of(2000, 1, 1);
    private static final LocalDate FECHA_MAXIMA = LocalDate.of(2100, 12, 31);
    private static final int TAMANIO_MAXIMO = 100;

    private final JpaPagoRepository jpaPagoRepository;
    private final PagoMapper pagoMapper;

    @Transactional(readOnly = true)
    public Page<PagoResponse> consultarPagos(LocalDate desde, LocalDate hasta, String alumno,
            String estado, int page, int size) {

        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new RecursoInvalidoException("La fecha desde no puede ser posterior a la fecha hasta.");
        }

        List<Boolean> estados = switch (estado == null ? "TODOS" : estado.toUpperCase()) {
            case "TODOS" -> List.of(false, true);
            case "REALIZADO" -> List.of(false);
            case "ANULADO" -> List.of(true);
            default -> throw new RecursoInvalidoException("El estado debe ser TODOS, REALIZADO o ANULADO.");
        };

        String texto = alumno == null || alumno.isBlank()
                ? "%"
                : "%" + alumno.trim().toLowerCase() + "%";

        PageRequest pagina = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), TAMANIO_MAXIMO),
                Sort.by(Sort.Order.desc("fechaPago"), Sort.Order.desc("id")));

        return jpaPagoRepository
                .buscar(desde != null ? desde : FECHA_MINIMA,
                        hasta != null ? hasta : FECHA_MAXIMA,
                        estados, texto, pagina)
                .map(pagoMapper::convertirAResponse);
    }
}
