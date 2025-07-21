package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
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

    /**
     * MÉTODO DE FÁBRICA ESTÁTICO (para CRIAÇÃO).
     * Este é o único ponto de entrada para criar um NOVO administrador.
     */
    public static Administrador criarNovo(String nome, String email, String senhaPlana, SenhaEncoderPort encoder) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do administrador não pode ser vazio.");
        }

        // CORREÇÃO: Repassa o encoder para o método de criação da Senha.
        return new Administrador(
                UUID.randomUUID(),
                nome,
                new Email(email),
                Senha.criar(senhaPlana, encoder), // <-- CHAMADA CORRIGIDA
                true,
                LocalDateTime.now()
        );
    }


    /**
     * CONSTRUTOR PÚBLICO (para RECONSTITUIÇÃO).
     * Usado exclusivamente pela camada de persistência para remontar um objeto.
     */
    public Administrador(UUID id, String nome, Email email, Senha senha, Boolean ativo, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    /**
     * AÇÃO DE NEGÓCIO: Desativar o Administrador.
     * Retorna uma NOVA instância do administrador com o status 'ativo' como falso,
     * mantendo a imutabilidade do objeto original.
     */
    public Administrador desativar() {
        // Não altera o estado atual, cria um novo.
        return new Administrador(
                this.id,
                this.nome,
                this.email,
                this.senha, // A senha é a mesma, já criptografada
                false,     // O único campo que muda
                this.criadoEm
        );
    }

    // --- Getters ---
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Email getEmail() { return email; }
    public Senha getSenha() { return senha; }
    public Boolean getAtivo() { return ativo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}