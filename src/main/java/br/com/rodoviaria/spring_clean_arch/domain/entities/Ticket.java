package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket; // Corrigido para StatusTicket
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.*;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.DataCompraInvalidaException;

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
    private final StatusTicket status; // Corrigido para StatusTicket
    private final LocalDate dataCompra;
    private final Passageiro passageiro; // Relacionamento: Quem comprou o ticket
    private final Viagem viagem;       // Relacionamento: A qual viagem o ticket pertence

    public Ticket(UUID id, String nomePassageiroTicket, String documentoPassageiroTicket, int numeroAssento, BigDecimal preco, FormaPagamento formaPagamento, StatusTicket status, Passageiro passageiro, Viagem viagem) {
        // Validações de nulidade e estado
        if (id == null) throw new TicketInvalidoException("O ID do ticket não pode ser nulo.");
        if (nomePassageiroTicket == null || nomePassageiroTicket.isBlank()) throw new NomePassageiroTicketInvalidoException("O nome do passageiro no ticket não pode ser vazio.");
        if (documentoPassageiroTicket == null || documentoPassageiroTicket.isBlank()) throw new DocumentoPassageiroInvalidoException("O documento do passageiro no ticket não pode ser vazio.");
        if (preco == null || preco.compareTo(BigDecimal.ZERO) < 0) throw new PrecoInvalidoException("O preço do ticket não pode ser nulo ou negativo.");
        if (formaPagamento == null) throw new FormaPagamentoInvalidaException("A forma de pagamento não pode ser nula.");
        if (status == null) throw new StatusInvalidoException("O status do ticket não pode ser nulo.");
        if (passageiro == null) throw new TicketInvalidoException("O ticket deve estar associado a um comprador.");
        if (viagem == null) throw new TicketInvalidoException("O ticket deve estar associado a uma viagem.");

        // Validações de regras de negócio
        if (numeroAssento <= 0) throw new NumeroAssentoInvalidoException("O número do assento deve ser um valor positivo.");
        if (numeroAssento > viagem.getOnibus().getCapacidade()) throw new NumeroAssentoInvalidoException("O número do assento excede a capacidade do ônibus.");

        this.id = id;
        this.nomePassageiroTicket = nomePassageiroTicket;
        this.documentoPassageiroTicket = documentoPassageiroTicket;
        this.numeroAssento = numeroAssento;
        this.preco = preco;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.passageiro = passageiro;
        this.viagem = viagem;

        // Geração interna da data da compra e validação subsequente
        this.dataCompra = LocalDate.now();
        if (this.dataCompra.atStartOfDay().isAfter(viagem.getDataPartida())) {
            throw new DataCompraInvalidaException("Não é possível comprar um ticket para uma viagem que já partiu.");
        }
    }

    /**
     * Retorna uma nova instância do Ticket com o status alterado para CANCELADO.
     * Este método é usado para implementar o "Soft Delete".
     * @return um novo objeto Ticket com o status de cancelado.
     */
    public Ticket cancelar() {
        return new Ticket(
                this.id,
                this.nomePassageiroTicket,
                this.documentoPassageiroTicket,
                this.numeroAssento,
                this.preco,
                this.formaPagamento,
                StatusTicket.CANCELADO, // A única alteração
                this.passageiro,
                this.viagem
        );
    }

    // --- Getters ---

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

    public StatusTicket getStatus() {
        return status;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public Viagem getViagem() {
        return viagem;
    }
}