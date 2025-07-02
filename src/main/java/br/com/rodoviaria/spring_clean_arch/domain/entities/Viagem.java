package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.DataHoraChegadaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.StatusViagemInvalidoException;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Viagem {
    private final UUID id;
    private final LocalDateTime dataPartida;
    private final LocalDateTime dataHoraChegada;
    private final StatusViagem statusViagem;
    private final Linha linha;
    private final Onibus onibus;

    public Viagem(UUID id, LocalDateTime data_partida, LocalDateTime data_hora_chegada, StatusViagem statusViagem, Linha linha, Onibus onibus) {
        this.id = id;
        // Validação da relação entre os atributos
        if (data_hora_chegada.isBefore(data_partida)) {
            throw new DataHoraChegadaInvalidaException("A data de chegada não pode ser anterior à data de partida.");
        }
        this.dataPartida = data_partida;
        this.dataHoraChegada = data_hora_chegada;
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
    public LocalDateTime getDataPartida() {
        return dataPartida;
    }
    public LocalDateTime getDataHoraChegada() {
        return dataHoraChegada;
    }
    public Linha getLinha() { return linha;}
    public StatusViagem getStatusViagem() {
        return statusViagem;
    }
    public Onibus getOnibus() {
        return onibus;
    }

    public Viagem cancelar(){
        return new Viagem(
                this.id,
                this.dataPartida,
                this.dataHoraChegada,
                StatusViagem.CANCELADA,
                this.linha,
                this.onibus
        );
    }

}
