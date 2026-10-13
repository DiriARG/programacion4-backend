package com.ironempire.service.turno;

import com.ironempire.dto.response.turno.TurnoPropioProfesorResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarTurnosPropiosProfesorService {

    private final JpaUsuarioRepository usuarioRepository;
    private final JpaTurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional(readOnly = true)
    public List<TurnoPropioProfesorResponse> consultarTurnosPropios(String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el profesor autenticado."));

        List<Turno> turnos = turnoRepository.findByProfesorId(profesor.getId());

        return turnos.stream()
                .map(turnoMapper::convertirATurnoPropioProfesorResponse)
                .toList();
    }
}
