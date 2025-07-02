package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.CapacidadeInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.PlacaInvalidaException;

import java.util.UUID;

public final class Onibus {
    private final UUID id;
    private final String placa;
    private final String modelo;
    private final int capacidade;

    public Onibus(UUID id, String placa, String modelo, int capacidade) {
        this.id = id;
        // Validação para a placa
        // Preciso criar um pacote para exceções
        if(placa == null || placa.isBlank()){
            throw new PlacaInvalidaException("Placa inválida: Não pode ser nula ou vazia");
        }
        this.placa = placa;
        this.modelo = modelo;
        // Validação para a capacidade
        // Preciso criar um pacote para exceções
        if(capacidade <= 0){
            throw new CapacidadeInvalidaException("Capacidade não pode ser negativa.");
        }
        this.capacidade = capacidade;
    }
    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }
    public String getModelo() {
        return modelo;
    }
    public int getCapacidade() {
        return capacidade;
    }

}
