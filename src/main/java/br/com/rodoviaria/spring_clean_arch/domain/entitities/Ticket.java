package br.com.rodoviaria.spring_clean_arch.domain.entitities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class Ticket {
    private final UUID id;
    private final String nomePassageiroTicket;
    private final String documentoPassageiroTicket;
    private final int numeroAssento;
    private final BigDecimal preco;
    private final FormaPagamento formaPagamento;
    private final StatusViagem status;
    private final LocalDate dataCompra;
    private final Passageiro passageiro; // Relacionamento 2: Quem comprou o ticket;
    private final Viagem viagem; // Relacionamento 1: A qual a viagem o ticket pertence

    public Ticket(UUID id, String nomePassageiroTicket, String documentoPassageiroTicket, int numeroAssento, BigDecimal preco, FormaPagamento formaPagamento, StatusViagem status, LocalDate dataCompra, Passageiro comprador, Viagem viagem) {
        this.id = id;
        // Validação Nome passageiro e Documento Passageiro
        if(nomePassageiroTicket.isBlank()){
            throw new IllegalArgumentException("O nome do passageiro está vazio no ticket.");
        }
        if(documentoPassageiroTicket.isBlank()){
            throw new IllegalArgumentException("O documento do passageiro está vazio no ticket.");
        }
        this.nomePassageiroTicket = nomePassageiroTicket;
        this.documentoPassageiroTicket = documentoPassageiroTicket;
        if(numeroAssento < 0){
            throw new IllegalArgumentException("O número do assento não pode ser negativo.");
        }
        this.numeroAssento = numeroAssento;
        // Validação do preço
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do ticket não pode ser nulo ou negativo.");
        }
        this.preco = preco;
        if( formaPagamento == null){
            throw new IllegalArgumentException("A forma de pagamento não pode ser nula");
        }
        this.formaPagamento = formaPagamento;
        if( dataCompra == null){
            throw new IllegalArgumentException("A data de compra não pode ser vazia");
        }
        this.dataCompra = dataCompra;
        if(status == null){
            throw new IllegalArgumentException("O status não pode ser nulo");
        }
        this.status = status;
        this.passageiro = comprador;
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
    public int getNumeroAssento() {
        return numeroAssento;
    }
    public BigDecimal getPreco() {
        return preco;
    }
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
    public LocalDate getDataCompra() {
        return dataCompra;
    }
    public StatusViagem getStatus() {
        return status;
    }
    public Passageiro getPassageiro() {
        return passageiro;
    }
    public Viagem getViagem() {
        return viagem;
    }
}
