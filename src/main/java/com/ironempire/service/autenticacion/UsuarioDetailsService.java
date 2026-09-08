package com.ironempire.service.autenticacion;

import com.ironempire.repository.JpaUsuarioRepository;
import com.ironempire.security.UsuarioDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final JpaUsuarioRepository usuarioRepository;

    public UsuarioDetailsService(JpaUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * Spring Security llama automáticamente a este método durante
     * la autenticación cuando necesita obtener los datos del usuario.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                /*
                 * Si el usuario existe, convierte el Usuario obtenido de la bd en un
                 * UsuarioDetails.
                 */
                .map(UsuarioDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Credenciales inválidas"));
    }
}
