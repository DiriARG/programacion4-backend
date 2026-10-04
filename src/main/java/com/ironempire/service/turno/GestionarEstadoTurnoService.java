package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.AlumnoInscriptoResponse;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.exception.RecursoInvalidoException;
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
public class GestionarEstadoTurnoService {

    private final JpaTurnoRepository turnoRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponse desactivarTurno(Long id) {

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró un turno con el ID: " + id));

        if (!turno.getActivo()) {
            throw new RecursoInvalidoException("El turno ya se encuentra inactivo.");
        }

        turno.setActivo(false);

        Turno turnoDesactivado = turnoRepository.save(turno);

        List<AlumnoInscriptoResponse> alumnos = alumnoTurnoRepository.findByTurnoId(id)
                .stream()
                .map(AlumnoTurno::getAlumno)
                .map(alumno -> new AlumnoInscriptoResponse(
                        alumno.getId(),
                        alumno.getNombre(),
                        alumno.getApellido(),
                        alumno.getDni()))
                .toList();

        return turnoMapper.convertirAResponse(turnoDesactivado, alumnos);
    }
}