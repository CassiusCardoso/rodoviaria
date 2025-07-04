package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.CapacidadeInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.PlacaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.onibus.Placa;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Onibus {
    private final UUID id;
    private final Placa placa;
    private final String modelo;
    private final int capacidade;
    private final Boolean ativo;
    private final LocalDateTime criadoEm;

    // Construtor público: gera a data de criação automaticamente
    public Onibus(UUID id, Placa placa, String modelo, int capacidade, boolean ativo) {
        this(id, placa, modelo, capacidade, ativo, LocalDateTime.now());
    }

    // Construtor privado: usado internamente para preservar a data de criação
    private Onibus(UUID id, Placa placa, String modelo, int capacidade, boolean ativo, LocalDateTime criadoEm) {
        if (placa == null) {
            throw new PlacaInvalidaException("A placa não pode ser nula.");
        }
        if(capacidade <= 0){
            throw new CapacidadeInvalidaException("Capacidade não pode ser negativa.");
        }
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.capacidade = capacidade;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public Placa getPlaca() {
        return placa;
    }
    public String getModelo() {
        return modelo;
    }
    public int getCapacidade() {
        return capacidade;
    }
    public Boolean getAtivo() { return ativo;}
    public LocalDateTime getCriadoEm() { return criadoEm;}
    /**
     * Retorna uma nova instância do Onibus com o atributo ativo alterado para false
     * Este método é usado para implementar o "Soft Delete".
     * @return um novo objeto Onibus com o false no atributo ativo
     */

    public Onibus desativar(){
        return new Onibus(
                this.id,
                this.placa,
                this.modelo,
                this.capacidade,
                false,
                this.criadoEm
        );
    }

}
