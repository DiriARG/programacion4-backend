package com.ironempire.service.rutina;

import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.RutinaMapper;
import com.ironempire.model.Rutina;
import com.ironempire.model.RutinaEjercicio;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaRutinaEjercicioRepository;
import com.ironempire.repository.JpaRutinaRepository;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarRutinaActualService {

    private final JpaRutinaRepository rutinaRepository;
    private final JpaRutinaEjercicioRepository rutinaEjercicioRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final RutinaMapper rutinaMapper;

    @Transactional(readOnly = true)
    public RutinaResponse consultarRutinaActual(String email) {

        Usuario alumno = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el usuario autenticado."));

        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RecursoInvalidoException("El usuario autenticado no es un alumno.");
        }

        Rutina rutina = rutinaRepository.findByAlumnoIdAndActivaTrue(alumno.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El alumno no posee una rutina activa."));

        List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository.findByRutinaId(rutina.getId());

        return rutinaMapper.convertirAResponse(rutina, ejercicios);
    }
}
