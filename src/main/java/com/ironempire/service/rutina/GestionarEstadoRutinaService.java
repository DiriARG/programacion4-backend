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

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GestionarEstadoRutinaService {

    private final JpaRutinaRepository rutinaRepository;
    private final JpaRutinaEjercicioRepository rutinaEjercicioRepository;
    private final JpaUsuarioRepository usuarioRepository;
    private final RutinaMapper rutinaMapper;

    @Transactional
    public RutinaResponse desactivarRutina(Long rutinaId, String email) {

        Usuario profesor = obtenerProfesor(email);

        Rutina rutina = obtenerRutinaVisibleParaProfesor(
                rutinaId,
                profesor.getId());

        if (!Boolean.TRUE.equals(rutina.getActiva())) {
            throw new RecursoInvalidoException(
                    "La rutina ya se encuentra inactiva.");
        }

        rutina.setActiva(false);

        Rutina rutinaDesactivada = rutinaRepository.save(rutina);

        List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository.findByRutinaId(rutinaId);

        return rutinaMapper.convertirAResponse(
                rutinaDesactivada,
                ejercicios);
    }

    @Transactional
    public RutinaResponse reactivarRutina(Long rutinaId, String email) {

        Usuario profesor = obtenerProfesor(email);

        Rutina rutina = obtenerRutinaVisibleParaProfesor(
                rutinaId,
                profesor.getId());

        if (Boolean.TRUE.equals(rutina.getActiva())) {
            throw new RecursoInvalidoException(
                    "La rutina ya se encuentra activa.");
        }

        boolean alumnoTieneOtraRutinaActiva = rutinaRepository
        .existsByAlumnoIdAndActivaTrue(rutina.getAlumno().getId());

        if (alumnoTieneOtraRutinaActiva) {
            throw new RecursoInvalidoException(
                    "No se puede reactivar la rutina porque el alumno ya tiene otra rutina activa.");
        }

        rutina.setActiva(true);

        Rutina rutinaReactivada = rutinaRepository.save(rutina);

        List<RutinaEjercicio> ejercicios = rutinaEjercicioRepository.findByRutinaId(rutinaId);

        return rutinaMapper.convertirAResponse(
                rutinaReactivada,
                ejercicios);
    }

    private Usuario obtenerProfesor(String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException(
                    "El usuario autenticado no es un profesor.");
        }

        return profesor;
    }

    private Rutina obtenerRutinaVisibleParaProfesor(
            Long rutinaId,
            Long profesorId) {

        Specification<Rutina> specification = RutinaSpecification.visiblesParaProfesor(profesorId)
                .and((root, query, cb) -> cb.equal(root.get("id"), rutinaId));

        return rutinaRepository.findOne(specification)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una rutina con el ID indicado."));
    }
}