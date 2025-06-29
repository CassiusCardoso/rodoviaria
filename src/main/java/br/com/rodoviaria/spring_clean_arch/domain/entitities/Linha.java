package br.com.rodoviaria.spring_clean_arch.domain.entitities;

import java.util.UUID;

public final class Linha {
    private final UUID id;
    private final String origem;
    private final String destino;
    private final int duracao_prevista_minutos;

    public Linha(UUID id, String origem, String destino, int duracao_prevista_minutos) {
        this.id = id;
        // Validação para destino e origem
        // Preciso criar um pacote para exceções
        if(destino.isBlank()){
            throw new IllegalArgumentException("O destino está vazio.");
        }
        if(origem.isBlank()){
            throw new IllegalArgumentException("O destino está vazio.");
        }
        this.origem = origem;
        this.destino = destino;
        // Validação para duração
        // Preciso criar um pacote para exceções
        if(duracao_prevista_minutos <= 0){
            throw new IllegalArgumentException("O tempo da viagem não poder ser igual ou inferior a 0");
        }
        this.duracao_prevista_minutos =  duracao_prevista_minutos;
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
    public int getDuracao_prevista_minutos() {
        return duracao_prevista_minutos;
    }
}
