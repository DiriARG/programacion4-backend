package com.ironempire.service.asistencia;

import com.ironempire.dto.response.asistencia.AsistenciaAlumnoResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.AsistenciaMapper;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaAsistenciaRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarAsistenciasAlumnoService {

    private final JpaAsistenciaRepository asistenciaRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final AsistenciaMapper asistenciaMapper;

    @Transactional(readOnly = true)
    public List<AsistenciaAlumnoResponse> consultarAsistenciasAlumno(String email) {

        Usuario alumno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario autenticado."));

        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RecursoInvalidoException("El usuario autenticado no es un alumno.");
        }

        return asistenciaRepository.findByAlumnoIdOrderByFechaDescHoraDesc(alumno.getId())
                .stream()
                .map(asistenciaMapper::convertirAAsistenciaAlumnoResponse)
                .toList();
    }
}