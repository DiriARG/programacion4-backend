package com.ironempire.security;

import com.ironempire.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adaptador entre la entidad Usuario y Spring Security.
 * Spring Security trabaja con la interfaz UserDetails para obtener
 * la información necesaria para autenticar y autorizar usuarios.
 * Esta clase toma al Usuario y le proporciona a Spring Security
 * los datos que necesita: email, contraseña, rol y estado de la cuenta.
 */
public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    /*
     * Recibe un Usuario de la bd y lo adapta al formato que entiende Spring
     * Security.
     */
    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /*
     * Devuelve los permisos/roles que tiene el usuario.
     *
     * Se agrega el prefijo "ROLE_" porque es la convención que utiliza
     * Spring Security al trabajar con roles.
     *
     * Ejemplo:
     * ALUMNO → ROLE_ALUMNO
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + usuario.getRol().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPasswordHash();
    }

    // Se utilizará el email como nombre de usuario.
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}