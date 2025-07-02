package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.DestinoInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.DuracaoPrevistaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.OrigemInvalidoException;

import java.util.UUID;

public final class Linha {
    private final UUID id;
    private final String origem;
    private final String destino;
    private final int duracaoPrevistaMinutos;

    public Linha(UUID id, String origem, String destino, int duracao_prevista_minutos) {
        this.id = id;
        // Validação para destino e origem
        // Preciso criar um pacote para exceções
        if(destino.isBlank()){
            throw new DestinoInvalidoException("O destino é inválido, pois está vazio.");
        }
        if(origem.isBlank()){
            throw new OrigemInvalidoException("A origem é invalida, pois está vazia.");
        }
        // Validação para a origem não ser igual ao destino
        if(origem.equals(destino)){
            throw new OrigemInvalidoException("A origem e o destino não podem ser o mesmo endereço.");
        }
        this.origem = origem;
        this.destino = destino;
        // Validação para duração
        // Preciso criar um pacote para exceções
        if(duracao_prevista_minutos <= 0){
            throw new DuracaoPrevistaException("O tempo da viagem não poder ser igual ou inferior a 0");
        }
        this.duracaoPrevistaMinutos =  duracao_prevista_minutos;
    }
    public UUID getId() {
        return id;
    }

    public String getOrigem() {
        return origem;
    }
    public String getDestino() {
        return destino;
    }
    public int getDuracaoPrevistaMinutos() {
        return duracaoPrevistaMinutos;
    }
}
