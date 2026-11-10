package com.ironempire.service.ejercicio;

import com.ironempire.dto.request.ejercicio.ConsultarEjerciciosRequest;
import com.ironempire.dto.response.ejercicio.EjercicioListadoPageResponse;
import com.ironempire.dto.response.ejercicio.EjercicioResponse;
import com.ironempire.mapper.EjercicioMapper;
import com.ironempire.model.Ejercicio;
import com.ironempire.repository.JpaEjercicioRepository;
import com.ironempire.specification.EjercicioSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarEjerciciosService {

    private final JpaEjercicioRepository ejercicioRepository;
    private final EjercicioMapper ejercicioMapper;

    @Transactional(readOnly = true)
    public EjercicioListadoPageResponse consultarEjercicios(ConsultarEjerciciosRequest filtros) {

        Specification<Ejercicio> specification = EjercicioSpecification.conFiltros(filtros);

        Sort orden = Sort.by(
                Sort.Order.asc("nombre"),
                Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                orden);

        Page<Ejercicio> pagina = ejercicioRepository.findAll(specification, pageable);

        Page<EjercicioResponse> paginaResponse = pagina.map(ejercicioMapper::convertirAResponse);

        return new EjercicioListadoPageResponse(
                paginaResponse.getContent(),
                paginaResponse.getNumber(),
                paginaResponse.getSize(),
                paginaResponse.getTotalElements(),
                paginaResponse.getTotalPages(),
                paginaResponse.isFirst(),
                paginaResponse.isLast(),
                paginaResponse.isEmpty());
    }
}