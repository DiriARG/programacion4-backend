package com.ironempire.service.rutina;

import com.ironempire.dto.request.rutina.ConsultarRutinasRequest;
import com.ironempire.dto.response.rutina.RutinaListadoPageResponse;
import com.ironempire.dto.response.rutina.RutinaListadoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.RutinaMapper;
import com.ironempire.model.Rutina;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaRutinaRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.specification.RutinaSpecification;

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
public class ConsultarRutinasService {

    private final JpaRutinaRepository rutinaRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final RutinaMapper rutinaMapper;

    @Transactional(readOnly = true)
    public RutinaListadoPageResponse consultarRutinas(
            ConsultarRutinasRequest filtros,
            String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException( "No se encontró el usuario autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException("El usuario autenticado no es un profesor.");
        }

 
        Specification<Rutina> specification = RutinaSpecification.conFiltros(filtros, profesor.getId());

        Sort orden = Sort.by(
                Sort.Order.desc("fechaCreacion"),
                Sort.Order.desc("id"));

        Pageable pageable = PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                orden);

        Page<Rutina> pagina = rutinaRepository.findAll(specification, pageable);

        Page<RutinaListadoResponse> paginaResponse = pagina.map(rutinaMapper::convertirAListadoResponse);

        return new RutinaListadoPageResponse(
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