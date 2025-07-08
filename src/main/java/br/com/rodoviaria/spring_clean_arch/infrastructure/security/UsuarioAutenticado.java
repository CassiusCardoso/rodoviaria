package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Senha;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UsuarioAutenticado implements UserDetails {
    private final UUID id;
    private final String email;
    private final String senha;
    private final String nome;
    private final Role role;
    private final Boolean ativo;

    // Um construtor que recebe a entidade de domínio completa é muito útil
    public UsuarioAutenticado(Passageiro passageiro) {
        this.id = passageiro.getId();
        // A "tradução" do Value Object para String acontece AQUI, no construtor.
        // Você "desembrulha" o valor seguro do seu VO para entregar à camada de infra.
        this.email = passageiro.getEmail().getEmail();
        this.senha = passageiro.getSenha().getSenhaHash();
        this.nome = passageiro.getNome();
        this.role = passageiro.getRole();
        this.ativo = passageiro.getAtivo();
    }

    // Getters para os campos que precisa acessar no Controller (ex: via @AuthenticationPrincipal)
    public UUID getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    // --- MÉTODOS DA INTERFACE UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 2. Mapeia a sua Role para o formato que o Spring Security entende
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha (hash)
    }

    @Override
    public String getUsername() {
        return this.email; // O "username" no nosso caso é o email
    }

    // 3. Para os métodos abaixo, você pode simplesmente retornar true
    //    a menos que tenha regras de negócio específicas (ex: conta expira).
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

    @Override
    public boolean isEnabled() {
        return this.ativo; // Usa o status 'ativo' do seu passageiro
    }

}
