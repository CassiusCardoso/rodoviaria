package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "passageiros")
public class PassageiroModel {
    @Id
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean ativo;
    private LocalDateTime criadoEm;

    // RELACIONAMENTO: Um Passageiro pode ter muitos Tickets.
    // cascade = CascadeType.ALL: Se um passageiro for salvo, seus tickets também serão.
    // fetch = FetchType.LAZY: Os tickets só serão carregados do banco quando forem explicitamente solicitados.
    @OneToMany(mappedBy = "passageiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketModel> tickets;

    public PassageiroModel(){}
    public PassageiroModel(UUID id, String nome, String email, String senha, String cpf, String telefone, Role role, Boolean ativo, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.role = role;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}