package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;

import java.time.LocalDateTime;
import java.util.UUID;

public class Administrador {
    private final UUID id;
    private final String nome;
    private final Email email;
    private final Senha senha;
    private final Boolean ativo;
    private final LocalDateTime criadoEm;

    // Construtores (público e privado para manter imutabilidade do criadoEm)
    public Administrador(UUID id, String nome, Email email, Senha senha, Boolean ativo) {
        this(id, nome, email, senha, ativo, LocalDateTime.now());
    }

    private Administrador(UUID id, String nome, Email email, Senha senha, Boolean ativo, LocalDateTime criadoEm) {
        // Adicione validações se necessário
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public Senha getSenha() {
        return senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public Administrador desativar() {
        return new Administrador(
                this.id,
                this.nome,
                this.email,
                this.senha,
                false,
                this.criadoEm
        );
    }
}
