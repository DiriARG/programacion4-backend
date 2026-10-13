package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.TurnoPropioAlumnoResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.AlumnoTurno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAlumnoTurnoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarTurnosPropiosAlumnoService {

    private final JpaUsuarioRepository usuarioRepository;
    private final JpaAlumnoTurnoRepository alumnoTurnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional(readOnly = true)
    public List<TurnoPropioAlumnoResponse> consultarTurnosPropios(String email) {

        Usuario alumno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el alumno autenticado."));

        return alumnoTurnoRepository
                .findByAlumnoIdAndTurnoActivoTrue(alumno.getId())
                .stream()
                // Obtiene el turno asociado a cada inscripción del alumno.
                .map(AlumnoTurno::getTurno)
                .map(turnoMapper::convertirATurnoPropioAlumnoResponse)
                .toList();
    }
}