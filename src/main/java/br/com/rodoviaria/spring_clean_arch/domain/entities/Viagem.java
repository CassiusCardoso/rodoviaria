package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exception.viagem.DataHoraChegadaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exception.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exception.viagem.StatusViagemInvalidoException;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Viagem {
    private final UUID id;
    private final LocalDateTime data_partida;
    private final LocalDateTime data_hora_chegada;
    private final StatusViagem statusViagem;
    private final Linha linha;
    private final Onibus onibus;

    public Viagem(UUID id, LocalDateTime data_partida, LocalDateTime data_hora_chegada, StatusViagem statusViagem, Linha linha, Onibus onibus) {
        this.id = id;
        // Validação da relação entre os atributos
        if (data_hora_chegada.isBefore(data_partida)) {
            throw new DataHoraChegadaInvalidaException("A data de chegada não pode ser anterior à data de partida.");
        }
        this.data_partida = data_partida;
        this.data_hora_chegada = data_hora_chegada;
        if(statusViagem == null){
            throw new StatusViagemInvalidoException("O status da viagem não pode ser vazio.");
        }
        this.statusViagem = statusViagem;

        if (linha == null) {
            throw new LinhaInvalidaException("A viagem deve ter uma linha associada.");
        }
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
    public StatusViagem getStatusViagem() {
        return statusViagem;
    }
    public Onibus getOnibus() {
        return onibus;
    }

}
