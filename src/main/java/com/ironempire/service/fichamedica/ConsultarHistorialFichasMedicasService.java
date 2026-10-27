package com.ironempire.service.fichamedica;

import com.ironempire.dto.request.fichamedica.ConsultarHistorialFichasMedicasRequest;
import com.ironempire.dto.response.fichamedica.FichaMedicaHistorialPageResponse;
import com.ironempire.dto.response.fichamedica.FichaMedicaHistorialResponse;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.mapper.FichaMedicaMapper;
import com.ironempire.model.FichaMedica;
import com.ironempire.repository.JpaFichaMedicaRepository;
import com.ironempire.specification.FichaMedicaSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultarHistorialFichasMedicasService {

    private final JpaFichaMedicaRepository fichaMedicaRepository;
    private final FichaMedicaMapper fichaMedicaMapper;

    @Transactional(readOnly = true)
    public FichaMedicaHistorialPageResponse consultar(ConsultarHistorialFichasMedicasRequest filtros) {

        validarRangoFechas(filtros);

        LocalDate fechaReferencia = LocalDate.now();

        /*
         * Las que vencen primero aparecen arriba, si dos o más fichas médicas tienen
         * exactamente la misma fecha de vencimiento, se ordenará basandose en su ID de
         * forma ascendente (id más bajo primero).
         */
        Sort orden = Sort.by(
                Sort.Order.asc("fechaVencimiento"),
                Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                orden);

        Specification<FichaMedica> specification = FichaMedicaSpecification.conFiltros(filtros, fechaReferencia);

        Page<FichaMedica> pagina = fichaMedicaRepository.findAll(specification, pageable);

        /*
         * Se utiliza una lambda porque el mapper necesita dos argumentos: la ficha
         * médica que se está convirtiendo y la fecha de referenciacon la que se
         * determina si está vigente o vencida.
         * Como Page.map() proporciona únicamente cada ficha, la lambda (->) agrega la
         * fecha de referencia que se obtuvo al comienzo de la consulta.
         * Así, todas las fichas se evalúan con la misma fecha, evitando que el estado
         * calculado y los filtros de la consulta utilicen fechas distintas.
         */
        Page<FichaMedicaHistorialResponse> paginaResponse = pagina.map(
                ficha -> fichaMedicaMapper.convertirAHistorialResponse(
                        ficha,
                        fechaReferencia));

        return new FichaMedicaHistorialPageResponse(
                paginaResponse.getContent(),
                paginaResponse.getNumber(),
                paginaResponse.getSize(),
                paginaResponse.getTotalElements(),
                paginaResponse.getTotalPages(),
                paginaResponse.isFirst(),
                paginaResponse.isLast(),
                paginaResponse.isEmpty());
    }

    private void validarRangoFechas(ConsultarHistorialFichasMedicasRequest filtros) {

        LocalDate fechaDesde = filtros.getFechaVencimientoDesde();
        LocalDate fechaHasta = filtros.getFechaVencimientoHasta();

        if (fechaDesde != null
                && fechaHasta != null
                && fechaDesde.isAfter(fechaHasta)) {

            throw new RecursoInvalidoException(
                    "La fecha de vencimiento desde no puede ser posterior "
                            + "a la fecha de vencimiento hasta.");
        }
    }
}