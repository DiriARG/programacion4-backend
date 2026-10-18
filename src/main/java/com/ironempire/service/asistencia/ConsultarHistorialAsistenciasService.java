package com.ironempire.service.asistencia;

import com.ironempire.dto.request.asistencia.ConsultarAsistenciasHistorialRequest;
import com.ironempire.dto.response.asistencia.AsistenciaHistorialPageResponse;
import com.ironempire.dto.response.asistencia.AsistenciaHistorialResponse;
import com.ironempire.enums.ModalidadAsistencia;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.AsistenciaMapper;
import com.ironempire.model.Asistencia;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAsistenciaRepository;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.specification.AsistenciaSpecification;

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
public class ConsultarHistorialAsistenciasService {

    private final JpaAsistenciaRepository asistenciaRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final JpaTurnoRepository turnoRepository;
    private final AsistenciaMapper asistenciaMapper;

    private static final String MENSAJE_TURNO_NO_ENCONTRADO = "No se encontró un turno propio con el ID indicado.";

    @Transactional(readOnly = true)
    public AsistenciaHistorialPageResponse consultarHistorialAdministrativo(
            ConsultarAsistenciasHistorialRequest filtros) {

        validarFiltros(filtros);

        Specification<Asistencia> specification = AsistenciaSpecification.conFiltros(filtros);

        Pageable pageable = crearPageable(filtros);

        // Aplica los filtros de búsqueda y la páginación.
        Page<Asistencia> pagina = asistenciaRepository.findAll(specification, pageable);

        return convertirPagina(pagina);
    }

    @Transactional(readOnly = true)
    public AsistenciaHistorialPageResponse consultarHistorialProfesor(
            ConsultarAsistenciasHistorialRequest filtros,
            String email) {

        validarFiltros(filtros);

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException("El usuario autenticado no es un profesor.");
        }

        if (filtros.getTurnoId() != null) {
            validarTurnoPropio(filtros.getTurnoId(), profesor.getId());
        }

        /*
         * Combina los filtros solicitados con las restricciones de visibilidad del
         * profesor.
         */
        Specification<Asistencia> specification = AsistenciaSpecification.conFiltros(filtros)
                .and(AsistenciaSpecification.visiblesParaProfesor(
                        profesor.getId()));

        Pageable pageable = crearPageable(filtros);

        Page<Asistencia> pagina = asistenciaRepository.findAll(specification, pageable);

        return convertirPagina(pagina);
    }

    // Valida las combinaciones de filtros antes de consultar la base de datos.
    private void validarFiltros(ConsultarAsistenciasHistorialRequest filtros) {

        if (filtros.getFechaDesde() != null
                && filtros.getFechaHasta() != null
                && filtros.getFechaDesde().isAfter(filtros.getFechaHasta())) {

            throw new RecursoInvalidoException("La fecha desde no puede ser posterior a la fecha hasta.");
        }

        // Si no se especifica modalidad, se consideran todas.
        if (filtros.getModalidad() == null) {
            filtros.setModalidad(ModalidadAsistencia.TODAS);
        }

        // Un entrenamiento libre no puede estar asociado a un turno.
        if (filtros.getModalidad() == ModalidadAsistencia.LIBRE
                && filtros.getTurnoId() != null) {

            throw new RecursoInvalidoException("No se puede indicar un turno cuando la modalidad es LIBRE.");
        }

        if (filtros.getTurnoId() != null && filtros.getTurnoId() <= 0) {
            throw new RecursoInvalidoException("El ID del turno debe ser mayor que cero.");
        }

    }

    // Verifica que el turno exista y pertenezca al profesor autenticado.
    private void validarTurnoPropio(Long turnoId, Long profesorId) {

        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(MENSAJE_TURNO_NO_ENCONTRADO));

        if (!turno.getProfesor().getId().equals(profesorId)) {
            throw new RecursoNoEncontradoException(MENSAJE_TURNO_NO_ENCONTRADO);
        }
    }

    /*
     * Crea la paginación con un orden estable:
     * fecha más reciente, hora más reciente e ID descendente.
     */
    private Pageable crearPageable(
            ConsultarAsistenciasHistorialRequest filtros) {

        Sort orden = Sort.by(
                Sort.Order.desc("fecha"),
                Sort.Order.desc("hora"),
                Sort.Order.desc("id"));

        return PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                orden);
    }

    private AsistenciaHistorialPageResponse convertirPagina(Page<Asistencia> pagina) {

        Page<AsistenciaHistorialResponse> paginaResponse = pagina.map(asistenciaMapper::convertirAHistorialResponse);

        /*
         * Se construye la respuesta con los registros de la página y los metadatos que
         * necesita el front para navegar el historial.
         * Ej: Si hay 27 asistencias que cumplen los filtros y se solicitan 10 por
         * página, la respuesta de la primera página tendrá 10 registros, totalElements
         * = 27 y totalPages = 3.
         * Si se solicita una página que no contiene registros, content estará vacío y
         * empty será true.
         */
        return new AsistenciaHistorialPageResponse(
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