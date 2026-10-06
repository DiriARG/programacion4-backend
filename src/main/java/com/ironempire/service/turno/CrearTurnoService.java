package com.ironempire.service.turno;

import com.ironempire.dto.request.turno.CrearTurnoRequest;
import com.ironempire.dto.response.turno.TurnoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.mapper.TurnoMapper;
import com.ironempire.model.Turno;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaTurnoRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrearTurnoService {

    private final ValidarUsuarioService validarUsuarioService;
    private final JpaTurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;

    @Transactional
    public TurnoResponse crearTurno(CrearTurnoRequest request) {

        Usuario profesor = validarUsuarioService.validarUsuario(request.getProfesorId(), Rol.PROFESOR, "profesor");

        if (!profesor.getActivo()) {
            throw new RecursoInvalidoException("El profesor se encuentra inactivo.");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new RecursoInvalidoException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        boolean existeSuperposicion = turnoRepository
                .existsByProfesorIdAndDiaSemanaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(
                        profesor.getId(),
                        request.getDiaSemana(),
                        request.getHoraFin(),
                        request.getHoraInicio());

        if (existeSuperposicion) {
            throw new RecursoInvalidoException(
                    "El profesor ya posee un turno activo superpuesto en ese día y horario.");
        }

        Turno nuevoTurno = new Turno();
        nuevoTurno.setProfesor(profesor);
        nuevoTurno.setNombreClase(request.getNombreClase());
        nuevoTurno.setDiaSemana(request.getDiaSemana());
        nuevoTurno.setHoraInicio(request.getHoraInicio());
        nuevoTurno.setHoraFin(request.getHoraFin());
        nuevoTurno.setActivo(true);

        Turno turnoGuardado = turnoRepository.save(nuevoTurno);

        /*
         * Lista vacía porque el turno recién creado todavía no tiene alumnos
         * inscriptos.
         */
        return turnoMapper.convertirAResponse(turnoGuardado, List.of());
    }
}