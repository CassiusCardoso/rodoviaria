package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="tickets")
public class TicketModel {
    @Id
    // --- ADICIONE ESTA ANOTAÇÃO ---
    @GeneratedValue(strategy = GenerationType.UUID) // Diz ao BD para gerar o UUID
    private UUID id;
    private String nomePassageiroTicket;
    private String documentoPassageiroTicket;
    private int numeroAssento;
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusTicket status;

    private LocalDate dataCompra;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "passageiro_id")
    private PassageiroModel passageiro;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "viagem_id")
    private ViagemModel viagem;

    // Construtor padrão
    public TicketModel(){}

    // Construtores, Getters e Setters...
    public TicketModel(UUID id, String nomePassageiroTicket, String documentoPassageiroTicket, int numeroAssento, BigDecimal preco, FormaPagamento formaPagamento, StatusTicket status, LocalDate dataCompra, PassageiroModel passageiro, ViagemModel viagem) {
        this.id = id;
        this.nomePassageiroTicket = nomePassageiroTicket;
        this.documentoPassageiroTicket = documentoPassageiroTicket;
        this.numeroAssento = numeroAssento;
        this.preco = preco;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.dataCompra = dataCompra;
        this.passageiro = passageiro;
        this.viagem = viagem;
    }
    public UUID getId() {
        return id;
    }
    public String getNomePassageiroTicket() {
        return nomePassageiroTicket;
    }
    public String getDocumentoPassageiroTicket() {
        return documentoPassageiroTicket;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public int getNumeroAssento() {
        return numeroAssento;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public PassageiroModel getPassageiro() {
        return passageiro;
    }

    public StatusTicket getStatus() {
        return status;
    }

    public ViagemModel getViagem() {
        return viagem;
    }

    // EDIT 18:01 08/07
    // Setters para todos (menos para id e criadoEm)


    public void setNomePassageiroTicket(String nomePassageiroTicket) {
        this.nomePassageiroTicket = nomePassageiroTicket;
    }

    public void setDocumentoPassageiroTicket(String documentoPassageiroTicket) {
        this.documentoPassageiroTicket = documentoPassageiroTicket;
    }

    public void setNumeroAssento(int numeroAssento) {
        this.numeroAssento = numeroAssento;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setStatus(StatusTicket status) {
        this.status = status;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public void setPassageiro(PassageiroModel passageiro) {
        this.passageiro = passageiro;
    }

    public void setViagem(ViagemModel viagem) {
        this.viagem = viagem;
    }
}

