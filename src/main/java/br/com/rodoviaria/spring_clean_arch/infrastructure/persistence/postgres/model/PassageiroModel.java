package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "passageiros")
public class PassageiroModel {
    @Id
    // --- ADICIONE ESTA ANOTAÇÃO ---
    @GeneratedValue(strategy = GenerationType.UUID) // Diz ao BD para gerar o UUID
    private UUID id;
    private String nome;

    @Column(unique = true)
    private String email;
    private String senha;
    private String cpf;
    private String telefone;

    private Boolean ativo;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    // RELACIONAMENTO: Um Passageiro pode ter muitos Tickets.
    // cascade = CascadeType.ALL: Se um passageiro for salvo, seus tickets também serão.
    // fetch = FetchType.LAZY: Os tickets só serão carregados do banco quando forem explicitamente solicitados.
    @OneToMany(mappedBy = "passageiro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketModel> tickets;

    public PassageiroModel(){}
    public PassageiroModel(UUID id, String nome, String email, String senha, String cpf, String telefone, Boolean ativo, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    // EDIT 18:01 08/07
    // Setters para todos (menos para id e criadoEm)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }
}