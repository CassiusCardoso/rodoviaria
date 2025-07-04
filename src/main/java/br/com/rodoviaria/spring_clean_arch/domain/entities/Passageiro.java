package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.NomeInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Telefone;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Passageiro {

    private final UUID id;
    private final String nome;
    private final Email email;
    private final Senha senha;
    private final Cpf cpf;
    private final Telefone telefone;
    private final Role role;
    private final Boolean ativo;
    private final LocalDateTime criadoEm;

    // Construtor público
    public Passageiro(UUID id, String nome, Email email, Senha senha, Cpf cpf, Telefone telefone, Role role, Boolean ativo) {
        this(id, nome, email, senha, cpf, telefone, role, ativo, LocalDateTime.now());
    }
    // Construtor privado
    private Passageiro(UUID id, String nome, Email email, Senha senha, Cpf cpf, Telefone telefone, Role role, Boolean ativo, LocalDateTime criadoEm) {
        if(nome == null || nome.isEmpty()) throw new NomeInvalidoException("Nome vazio ou nulo.");

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.role = role;
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
    public Cpf getCpf() {
        return cpf;
    }
    public Telefone getTelefone() {
        return telefone;
    }
    public Role getRole() {
        return role;
    }
    public Boolean getAtivo() { return ativo;}
    public LocalDateTime getCriadoEm() { return criadoEm;}

    /**
     * Retorna uma nova instância do Passageiro com o atributo ativo alterado para false
     * Este método é usado para implementar o "Soft Delete".
     * @return um novo objeto Passageiro com o false no atributo ativo
     */

    public Passageiro desativar(){
        return new Passageiro(
                this.id,
                this.nome,
                this.email,
                this.senha,
                this.cpf,
                this.telefone,
                this.role,
                false, // <<-- Correção: Atribui diretamente o valor 'false', em vez de this.ativo = false
                this.criadoEm
        );
    }
}
