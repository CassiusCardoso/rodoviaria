package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "viagens")
public class ViagemModel {
    @Id
    private UUID id;
    private LocalDateTime dataPartida;
    private LocalDateTime dataHoraChegada;

    @Enumerated(EnumType.STRING)
    private StatusViagem statusViagem;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "linha_id")
    private LinhaModel linha;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "onibus_id")
    private OnibusModel onibus;

    // RELACIONAMENTO INVERSO: Uma viagem pode ter muitos tickets.
    @OneToMany(mappedBy = "viagem")
    private List<TicketModel> tickets;

    // Construtor padrão
    public ViagemModel() {
    }

    // Construtor com todos os campos
    public ViagemModel(UUID id, LocalDateTime dataPartida, LocalDateTime dataHoraChegada,
                       StatusViagem statusViagem, LinhaModel linha, OnibusModel onibus) {
        this.id = id;
        this.dataPartida = dataPartida;
        this.dataHoraChegada = dataHoraChegada;
        this.statusViagem = statusViagem;
        this.linha = linha;
        this.onibus = onibus;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDataPartida() {
        return dataPartida;
    }

    public LocalDateTime getDataHoraChegada() {
        return dataHoraChegada;
    }

    public StatusViagem getStatusViagem() {
        return statusViagem;
    }

    public LinhaModel getLinha() {
        return linha;
    }

    public OnibusModel getOnibus() {
        return onibus;
    }

    // EDIT 18:01 08/07
    // Setters para todos (menos para id e criadoEm)

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDataPartida(LocalDateTime dataPartida) {
        this.dataPartida = dataPartida;
    }

    public void setDataHoraChegada(LocalDateTime dataHoraChegada) {
        this.dataHoraChegada = dataHoraChegada;
    }

    public void setStatusViagem(StatusViagem statusViagem) {
        this.statusViagem = statusViagem;
    }

    public void setLinha(LinhaModel linha) {
        this.linha = linha;
    }

    public void setOnibus(OnibusModel onibus) {
        this.onibus = onibus;
    }

    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }
}
