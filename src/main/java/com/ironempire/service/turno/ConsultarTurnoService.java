package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoListadoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.AlumnoTurno;
import com.ironempire.model.Turno;
import com.ironempire.repository.JpaAlumnoTurnoRepository;
import com.ironempire.repository.JpaTurnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarTurnoService {

    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional(readOnly = true)
    public List<TurnoListadoResponse> consultarTurnos() {

        List<Turno> turnos = turnoRepository.findAll();

        return turnos.stream()
                .map(turnoMapper::convertirAListadoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TurnoResponse consultarTurno(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + id));
        
        // Busca las inscripciones correspondientes a un turno.
        List<AlumnoInscriptoResponse> alumnos = alumnoTurnoRepository.findByTurnoId(id)
                .stream()
                // Obtiene el Usuario (alumno) de cada relación ALUMNO_TURNO.
                .map(AlumnoTurno::getAlumno)
                // Convierte cada Usuario en el DTO del alumno inscripto.
                .map(alumno -> new AlumnoInscriptoResponse(
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellido(),
                        alumno.getDni()))
                // Junta todos los DTO en una lista.
                .toList();

        // Combina la información del turno con sus alumnos para construir el detalle.
        return turnoMapper.convertirAResponse(turno, alumnos);
    }
}