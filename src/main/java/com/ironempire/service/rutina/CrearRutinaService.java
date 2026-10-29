package com.ironempire.service.rutina;

import com.ironempire.dto.request.rutina.CrearRutinaRequest;
import com.ironempire.dto.response.rutina.RutinaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.exception.RecursoInvalidoException;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.RutinaMapper;
import com.ironempire.model.Ejercicio;
import com.ironempire.model.Rutina;
import com.ironempire.model.RutinaEjercicio;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaEjercicioRepository;
import com.ironempire.repository.JpaRutinaEjercicioRepository;
import com.ironempire.repository.JpaRutinaRepository;
import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.service.usuario.ValidarUsuarioService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearRutinaService {

    private final JpaUsuarioRepository usuarioRepository;
    private final JpaRutinaRepository rutinaRepository;
    private final JpaEjercicioRepository ejercicioRepository;
    private final JpaRutinaEjercicioRepository rutinaEjercicioRepository;
    private final ValidarUsuarioService validarUsuarioService;
    private final RutinaMapper rutinaMapper;

    @Transactional
    public RutinaResponse crearRutina(CrearRutinaRequest request, String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("No se encontró el perfil del profesor autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException("El usuario autenticado no es un profesor.");
        }

        Usuario alumno = validarUsuarioService.validarUsuario(
                request.getAlumnoId(),
                Rol.ALUMNO,
                "alumno");

        // Obtiene los identificadores de los ejercicios solicitados.
        List<Long> ejercicioIds = request.getEjercicios().stream()
                .map(ejercicio -> ejercicio.getEjercicioId())
                // Se elimina los IDs repetidos de la lista (1 mismo ejercicio en dos días
                // diferentes).
                .distinct()
                .toList();

        // Recupera del catálogo los ejercicios cuyos IDs se solicitaron.
        Map<Long, Ejercicio> ejerciciosPorId = ejercicioRepository
                .findAllById(ejercicioIds)
                // Permite procesar cada ejercicio recuperado individualmente.
                .stream()
                // Acumula los resultados del stream y construye un Map (clave-valor).
                .collect(Collectors.toMap(
                        // Se utiliza el ID del ejercicio como clave del Map.
                        Ejercicio::getId,
                        // Utiliza el propio objeto Ejercicio como valor del Map.
                        Function.identity()));

        for (Long ejercicioId : ejercicioIds) {

            Ejercicio ejercicio = ejerciciosPorId.get(ejercicioId);

            if (ejercicio == null) {
                throw new RecursoNoEncontradoException("No se encontró un ejercicio con el ID: " + ejercicioId);
            }

            if (!Boolean.TRUE.equals(ejercicio.getActivo())) {
                throw new RecursoInvalidoException(
                        "El ejercicio con ID " + ejercicioId
                                + " está inactivo y no puede incorporarse a una rutina.");
            }
        }

        /*
         * Desactiva la rutina anterior para conservar una única rutina activa. El
         * método del repositorio devuelve un Optional por lo tanto se puede o no
         * encontrar una rutina activa, si no encuentra no hace nada y el service
         * continúa.
         */
        rutinaRepository.findByAlumnoIdAndActivaTrue(alumno.getId())
                .ifPresent(rutinaActiva -> {
                    rutinaActiva.setActiva(false);
                    rutinaRepository.save(rutinaActiva);
                });

        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setAlumno(alumno);
        nuevaRutina.setProfesor(profesor);
        nuevaRutina.setNombre(request.getNombre());
        nuevaRutina.setFechaCreacion(LocalDate.now());
        nuevaRutina.setActiva(true);

        Rutina rutinaGuardada = rutinaRepository.save(nuevaRutina);

        // Construye una asociación RutinaEjercicio por cada ejercicio recibido en la petición.
        List<RutinaEjercicio> asociaciones = request.getEjercicios()
                .stream()
                .map(ejercicioRequest -> {

                    RutinaEjercicio asociacion = new RutinaEjercicio();

                    asociacion.setRutina(rutinaGuardada);
                    asociacion.setEjercicio(ejerciciosPorId.get(ejercicioRequest.getEjercicioId()));
                    asociacion.setSeries(ejercicioRequest.getSeries());
                    asociacion.setRepeticiones(ejercicioRequest.getRepeticiones());
                    asociacion.setPeso(ejercicioRequest.getPeso());
                    asociacion.setDiaSemana(ejercicioRequest.getDiaSemana());
                    asociacion.setOrden(ejercicioRequest.getOrden());

                    return asociacion;
                })
                .toList();

        List<RutinaEjercicio> asociacionesGuardadas = rutinaEjercicioRepository.saveAll(asociaciones);

        // Ordena los ejercicios por día y por orden de ejecución.
        List<RutinaEjercicio> asociacionesOrdenadas = asociacionesGuardadas
                .stream()
                .sorted(Comparator
                        .comparing(RutinaEjercicio::getDiaSemana)
                        .thenComparing(RutinaEjercicio::getOrden))
                .toList();

        return rutinaMapper.convertirAResponse(
                rutinaGuardada,
                asociacionesOrdenadas);
    }
}