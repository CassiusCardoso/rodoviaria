package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "administradores")
public class AdministradorModel {
    @Id
    private UUID id;
    private String nome;
    @Column(unique = true) // Email deve ser único
    private String email;
    private String senha;
    private Boolean ativo;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    public AdministradorModel(){}

    public AdministradorModel(UUID id, String nome, String email, String senha, Boolean ativo, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }
    // EDIT 09:36 09/07
    // Setters para todos (menos para id e criadoEm)
    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
