package com.ironempire.service.rutina;

import com.ironempire.dto.request.rutina.ModificarRutinaEjercicioRequest;
import com.ironempire.dto.request.rutina.ModificarRutinaRequest;
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
import com.ironempire.specification.RutinaSpecification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModificarRutinaService {

    private final JpaUsuarioRepository usuarioRepository;
    private final JpaRutinaRepository rutinaRepository;
    private final JpaRutinaEjercicioRepository rutinaEjercicioRepository;
    private final JpaEjercicioRepository ejercicioRepository;
    private final RutinaMapper rutinaMapper;

    @Transactional
    public RutinaResponse modificarRutina(
            Long rutinaId,
            ModificarRutinaRequest request,
            String email) {

        Usuario profesor = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario autenticado."));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RecursoInvalidoException("El usuario autenticado no es un profesor.");
        }

        Specification<Rutina> specification = RutinaSpecification.visiblesParaProfesor(profesor.getId())
                .and((root, query, cb) -> cb.equal(root.get("id"), rutinaId));

        Rutina rutina = rutinaRepository.findOne(specification)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una rutina con el ID indicado."));

        if (!Boolean.TRUE.equals(rutina.getActiva())) {
            throw new RecursoInvalidoException(
                    "No se puede modificar una rutina inactiva.");
        }
        /*
         * Recupera las asociaciones actuales entre la rutina y los ejercicios del
         * catálogo.
         */
        List<RutinaEjercicio> asociacionesActuales = rutinaEjercicioRepository.findByRutinaId(rutinaId);

        // Construye un mapa para localizar rápidamente las asociaciones por su ID.
        Map<Long, RutinaEjercicio> asociacionesPorId = asociacionesActuales
                .stream()
                .collect(Collectors.toMap(
                        RutinaEjercicio::getId,
                        Function.identity()));

        /*
         * Obtiene los IDs de los ejercicios nuevos que se quieren incorporar.
         * Se identifican porque todavía no tienen una asociación con la rutina,
         * por lo que rutinaEjercicioId es null.
         */
        List<Long> ejercicioIdsNuevos = request.getEjercicios()
                .stream()
                .filter(ejercicio -> ejercicio.getRutinaEjercicioId() == null)
                .map(ModificarRutinaEjercicioRequest::getEjercicioId)
                .distinct()
                .toList();

        // Recupera y mapea los ejercicios nuevos del catálogo utilizando sus IDs.
        Map<Long, Ejercicio> ejerciciosPorId = ejercicioRepository
                .findAllById(ejercicioIdsNuevos)
                .stream()
                .collect(Collectors.toMap(
                        Ejercicio::getId,
                        Function.identity()));

        // Validaciones a los ejercicios nuevos.
        for (Long ejercicioId : ejercicioIdsNuevos) {

            Ejercicio ejercicio = ejerciciosPorId.get(ejercicioId);

            if (ejercicio == null) {
                throw new RecursoNoEncontradoException(
                        "No se encontró un ejercicio con el ID: " + ejercicioId);
            }

            if (!Boolean.TRUE.equals(ejercicio.getActivo())) {
                throw new RecursoInvalidoException(
                        "El ejercicio con ID " + ejercicioId
                                + " está inactivo y no puede incorporarse a una rutina.");
            }
        }

        /*
         * Almacena los IDs de las asociaciones existentes que fueron incluidas en la
         * solicitud de modificación.
         * Set es una colección de elementos únicos, en este caso IDs. Con HashSet se
         * crea la colección.
         */
        Set<Long> idsAsociacionesRecibidas = new HashSet<>();

        /*
         * Contiené las asociaciones nuevas y las existentes que se modifican
         * o se conservan.
         */
        List<RutinaEjercicio> asociacionesAGuardar = new ArrayList<>();

        // Procesa cada ejercicio informado en la solicitud.
        for (ModificarRutinaEjercicioRequest ejercicioRequest : request.getEjercicios()) {

            if (ejercicioRequest.getRutinaEjercicioId() != null) {

                Long asociacionId = ejercicioRequest.getRutinaEjercicioId();

                /*
                 * Intenta agregar el ID de la asociación al Set.
                 * El método .add() devuelve false si el elemento ya existía previamente en el
                 * Set.
                 * Por lo tanto, si ya estaba, el operador "!" invierte el resultado a true
                 * y dispara la excepción para evitar duplicados en la misma solicitud.
                 */
                if (!idsAsociacionesRecibidas.add(asociacionId)) {
                    throw new RecursoInvalidoException(
                            "La asociación con ID " + asociacionId
                                    + " se encuentra repetida en la solicitud.");
                }

                // Recupera la asociación (get) y verifica que pertenezca a esta rutina.
                RutinaEjercicio asociacion = asociacionesPorId.get(asociacionId);

                if (asociacion == null) {
                    throw new RecursoNoEncontradoException(
                            "No se encontró una asociación con el ID "
                                    + asociacionId
                                    + " dentro de la rutina indicada.");
                }

                actualizarDatosAsociacion(asociacion, ejercicioRequest);

                asociacionesAGuardar.add(asociacion);

            } else {

                Ejercicio ejercicio = ejerciciosPorId.get(ejercicioRequest.getEjercicioId());

                RutinaEjercicio nuevaAsociacion = new RutinaEjercicio();

                nuevaAsociacion.setRutina(rutina);
                nuevaAsociacion.setEjercicio(ejercicio);

                actualizarDatosAsociacion(nuevaAsociacion, ejercicioRequest);

                asociacionesAGuardar.add(nuevaAsociacion);
            }
        }

        // Identifica las asociaciones actuales que no fueron enviadas en la solicitud.
        List<RutinaEjercicio> asociacionesAEliminar = asociacionesActuales
                .stream()
                .filter(asociacion -> !idsAsociacionesRecibidas.contains(asociacion.getId()))
                .toList();

        rutina.setNombre(request.getNombre());

        Rutina rutinaModificada = rutinaRepository.save(rutina);

        List<RutinaEjercicio> asociacionesGuardadas = rutinaEjercicioRepository.saveAll(asociacionesAGuardar);

        if (!asociacionesAEliminar.isEmpty()) {
            rutinaEjercicioRepository.deleteAll(asociacionesAEliminar);
        }

        return rutinaMapper.convertirAResponse(
                rutinaModificada,
                asociacionesGuardadas);
    }

    // Actualiza los campos de la asociación entre una rutina y un ejercicio.
    private void actualizarDatosAsociacion(
            RutinaEjercicio asociacion,
            ModificarRutinaEjercicioRequest request) {

        asociacion.setSeries(request.getSeries());
        asociacion.setRepeticiones(request.getRepeticiones());
        asociacion.setPeso(request.getPeso());
        asociacion.setDiaSemana(request.getDiaSemana());
        asociacion.setOrden(request.getOrden());
    }
}
