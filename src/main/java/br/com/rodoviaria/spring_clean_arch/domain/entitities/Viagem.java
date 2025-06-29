package br.com.rodoviaria.spring_clean_arch.domain.entitities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Viagem {
    private final UUID id;
    private final LocalDateTime data_partida;
    private final LocalDateTime data_hora_chegada;
    private final StatusTicket statusViagem;
    private final Linha linha;
    private final Onibus onibus;

    public Viagem(UUID id, LocalDateTime data_partida, LocalDateTime data_hora_chegada, StatusTicket statusViagem, Linha linha, Onibus onibus) {
        this.id = id;
        // Validação da relação entre os atributos
        if (data_hora_chegada.isBefore(data_partida)) {
            throw new IllegalArgumentException("A data de chegada não pode ser anterior à data de partida.");
        }
        if (linha == null) {
            throw new IllegalArgumentException("A viagem deve ter uma linha associada.");
        }
        this.data_partida = data_partida;
        this.data_hora_chegada = data_hora_chegada;
        this.statusViagem = statusViagem;
        this.linha = linha;
        this.onibus = onibus;
    }
    public UUID getId() {
        return id;
    }

    public LocalDateTime getData_partida() {
        return data_partida;
    }
    public LocalDateTime getData_hora_chegada() {
        return data_hora_chegada;
    }
    public StatusTicket getStatusViagem() {
        return statusViagem;
    }
    public Onibus getOnibus() {
        return onibus;
    }

}
