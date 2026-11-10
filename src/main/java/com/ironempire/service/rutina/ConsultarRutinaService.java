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
import com.ironempire.specification.RutinaSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarRutinaService {

    private final JpaRutinaRepository rutinaRepository;
    private final JpaRutinaEjercicioRepository rutinaEjercicioRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final RutinaMapper rutinaMapper;

    @Transactional(readOnly = true)
    public RutinaResponse consultarRutina(Long rutinaId, String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException("El usuario autenticado no es un profesor.");
        }

        /*
         * Construye una consulta que verifica dos condiciones:
         * 1. El profesor autenticado está asociado al alumno propietario de la rutina.
         * 2. El ID de la rutina coincide con el recibido en la solicitud.
         * Esto se hace porque los profesores tienen un alcance segmentado y privado
         * sobre su ámbito privado de trabajo (en este caso las rutinas).
         */
        Specification<Rutina> specification = RutinaSpecification.visiblesParaProfesor(profesor.getId())
                .and((root, query, cb) -> cb.equal(root.get("id"), rutinaId));

        Rutina rutina = rutinaRepository.findOne(specification)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una rutina con el ID indicado."));

        // Recupera los ejercicios asociados a la rutina.
        List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository.findByRutinaId(rutinaId);

        return rutinaMapper.convertirAResponse(rutina, ejercicios);
    }
}