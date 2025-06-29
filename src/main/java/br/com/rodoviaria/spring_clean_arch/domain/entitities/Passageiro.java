package br.com.rodoviaria.spring_clean_arch.domain.entitities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.vo.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.vo.Email;
import br.com.rodoviaria.spring_clean_arch.domain.vo.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.vo.Telefone;

import java.util.UUID;

public final class Passageiro {

    private final UUID id;
    private final String nome;
    private final Email email;
    private final Senha senha;
    private final Cpf cpf;
    private final Telefone telefone;
    private final Role role;


    public Passageiro(UUID id, String nome, Email email, Senha senha, Cpf cpf, Telefone telefone, Role role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.role = role;
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

}
