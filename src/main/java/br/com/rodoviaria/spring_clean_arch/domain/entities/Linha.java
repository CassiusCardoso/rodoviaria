package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.DestinoInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.DuracaoPrevistaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha.OrigemInvalidoException;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Linha {
    private final UUID id;
    private final String origem;
    private final String destino;
    private final int duracaoPrevistaMinutos;
    private final Boolean ativo;
    private final LocalDateTime criadoEm;

    // Construtor público
    public Linha(UUID id, String origem, String destino, int duracaoPrevistaMinutos, Boolean ativo) {
        this(id, origem, destino, duracaoPrevistaMinutos, ativo, LocalDateTime.now());
    }

    // Construtor privado
    private Linha(UUID id, String origem, String destino, int duracaoPrevistaMinutos, Boolean ativo, LocalDateTime criadoEm) {
        if(destino.isBlank()) throw new DestinoInvalidoException("O destino é inválido, pois está vazio.");
        if(origem.isBlank()) throw new OrigemInvalidoException("A origem é invalida, pois está vazia.");
        if(origem.equals(destino)) throw new OrigemInvalidoException("A origem e o destino não podem ser o mesmo endereço.");
        if(duracaoPrevistaMinutos <= 0) throw new DuracaoPrevistaException("O tempo da viagem não poder ser igual ou inferior a 0");

        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.duracaoPrevistaMinutos = duracaoPrevistaMinutos;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
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
    public  Boolean getAtivo() { return ativo;}
    public LocalDateTime getCriadoEm() { return criadoEm;}

    public Linha desativar(){
        return new Linha(
                this.id,
                this.origem,
                this.destino,
                this.duracaoPrevistaMinutos,
                false,
                this.criadoEm
        );
    }
}
