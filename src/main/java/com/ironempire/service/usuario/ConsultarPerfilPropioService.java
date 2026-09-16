package com.ironempire.service.usuario;

import com.ironempire.dto.response.usuario.UsuarioResponse;
import com.ironempire.exception.RecursoNoEncontradoException;
import com.ironempire.mapper.UsuarioMapper;
import com.ironempire.model.Usuario;
import com.ironempire.repository.JpaUsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConsultarPerfilPropioService {

    private final JpaUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public UsuarioResponse consultarPerfilPropio(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el perfil del usuario autenticado."));

        return usuarioMapper.convertirAResponse(usuario);
    }
}