package br.com.rodoviaria.spring_clean_arch.domain.entitities;

import java.util.UUID;

public final class Onibus {
    private UUID id;
    private String placa;
    private String modelo;
    private int capacidade;

    public Onibus(){}
    public Onibus(UUID id, String placa, String modelo, int capacidade) {
        this.id = id;
        // Validação para a placa
        // Preciso criar um pacote para exceções
        if(placa == null || placa.isBlank()){
            throw new IllegalArgumentException("Placa inválida: Não pode ser nula ou vazia");
        }
        this.placa = placa;
        this.modelo = modelo;
        // Validação para a capacidade
        // Preciso criar um pacote para exceções
        if(capacidade <= 0){
            throw new IllegalArgumentException("Capacidade não pode ser negativa.");
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
