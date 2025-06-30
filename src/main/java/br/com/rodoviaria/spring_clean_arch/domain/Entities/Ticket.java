package br.com.rodoviaria.spring_clean_arch.domain.Entities;

import br.com.rodoviaria.spring_clean_arch.domain.Enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.Enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket.*;

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

    // Não vou passar o LocalDate dataCompra no construtor porque eu quero que ao criar um ticket, a data pegar a partir desse momento
    public Ticket(UUID id, String nomePassageiroTicket, String documentoPassageiroTicket, int numeroAssento, BigDecimal preco, FormaPagamento formaPagamento, StatusViagem status, Passageiro comprador, Viagem viagem) {
        this.id = id;
        // Validação Nome passageiro e Documento Passageiro
        if(nomePassageiroTicket.isBlank()){
            throw new NomePassageiroTicketInvalidoException("O nome do passageiro está vazio no ticket.");
        }
        if(documentoPassageiroTicket.isBlank()){
            throw new DocumentoPassageiroInvalidoException("O documento do passageiro está vazio no ticket.");
        }
        this.nomePassageiroTicket = nomePassageiroTicket;
        this.documentoPassageiroTicket = documentoPassageiroTicket;

        // Validação do numero de assento
        if(numeroAssento > viagem.getOnibus().getCapacidade()){
            throw new NumeroAssentoInvalidoException("O número do assento excede a capacidade do ônibus.");
        }
        if(numeroAssento < 0){
            throw new NumeroAssentoInvalidoException("O número do assento não pode ser negativo.");
        }
        this.numeroAssento = numeroAssento;
        // Validação do preço
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new PrecoInvalidoException("O preço do ticket não pode ser nulo ou negativo.");
        }
        this.preco = preco;
        if( formaPagamento == null){
            throw new FormaPagamentoInvalidaException("A forma de pagamento não pode ser nula");
        }
        this.formaPagamento = formaPagamento;
        this.dataCompra = LocalDate.now();
        if( dataCompra == null){
            throw new DataCompraInvalidaException("A data de compra não pode ser vazia");
        }
        if(this.dataCompra.atStartOfDay().isAfter(viagem.getDataPartida())){
            throw new DataCompraInvalidaException("Não é possível comprar um ticket para uma viagem que já partiu.");

        }
        if(status == null){
            throw new StatusInvalidoException("O status não pode ser nulo");
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
