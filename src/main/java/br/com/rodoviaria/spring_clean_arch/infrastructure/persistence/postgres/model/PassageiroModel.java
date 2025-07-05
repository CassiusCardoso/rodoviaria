package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "passageiros")
public class PassageiroModel {
    @Id
    private UUID id;
    // Para nome, email, cpf, senha e telefone não vamos utilizar ValueObjects. Vamos utilizar tipos primitivos
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;

    @Enumerated(EnumType.STRING) // Diz ao JPA para salvar o Enum como texto (ex: "ADMINISTRADOR")
    private Role role;
    private Boolean ativo;
    private LocalDateTime criadoEm;

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