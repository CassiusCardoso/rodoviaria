package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AdminAutenticado implements UserDetails {
    private final UUID id;
    private final String email;
    private final String senha;
    private final boolean ativo;

    public AdminAutenticado(Administrador administrador) {
        this.id = administrador.getId();
        this.email = administrador.getEmail().getEmail();
        this.senha = administrador.getSenha().getSenhaHash();
        this.ativo = administrador.getAtivo();
    }

    // Getters se precisar acessar no Controller
    public UUID getId() { return id; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Todo administrador tem a role de ADMIN
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}
