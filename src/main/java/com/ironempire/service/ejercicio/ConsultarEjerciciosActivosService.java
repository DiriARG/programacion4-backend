package com.ironempire.service.ejercicio;

import com.ironempire.dto.response.ejercicio.EjercicioActivoResponse;
import com.ironempire.repository.JpaEjercicioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarEjerciciosActivosService {

    private final JpaEjercicioRepository ejercicioRepository;

    @Transactional(readOnly = true)
    public List<EjercicioActivoResponse> consultarEjerciciosActivos() {

        return ejercicioRepository.findByActivoTrueOrderByNombreAscIdAsc()
                .stream()
                .map(ejercicio -> new EjercicioActivoResponse(
                        ejercicio.getId(),
                        ejercicio.getNombre(),
                        ejercicio.getGrupoMuscular()))
                .toList();
    }
}