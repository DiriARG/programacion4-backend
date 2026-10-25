package com.ironempire.service.fichamedica;

import com.ironempire.dto.response.fichamedica.AlumnoSinFichaMedicaResponse;
import com.ironempire.enums.Rol;
import com.ironempire.mapper.UsuarioMapper;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarAlumnosSinFichaMedicaService {

    private final JpaUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<AlumnoSinFichaMedicaResponse> consultar() {

        List<Usuario> alumnos = usuarioRepository.findUsuariosSinFichaMedicaPorRol(Rol.ALUMNO);

        return alumnos.stream()
                .map(usuarioMapper::convertirAAlumnoSinFichaMedicaResponse)
                .toList();
    }
}