package br.com.rodoviaria.spring_clean_arch.domain.entities;

import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.CapacidadeInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.PlacaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.onibus.Placa;

import java.util.UUID;

public final class Onibus {
    private final UUID id;
    private final Placa placa;
    private final String modelo;
    private final int capacidade;
    private final Boolean ativo;

    public Onibus(UUID id, Placa placa, String modelo, int capacidade,  boolean ativo) {
        this.id = id;
        // A validação da placa agora é garantida pelo próprio tipo!
        // Apenas precisamos verificar se o objeto não é nulo.
        if (placa == null) {
            throw new PlacaInvalidaException("A placa não pode ser nula.");
        }
        this.placa = placa;
        this.modelo = modelo;
        // Validação para a capacidade
        // Preciso criar um pacote para exceções
        if(capacidade <= 0){
            throw new CapacidadeInvalidaException("Capacidade não pode ser negativa.");
        }
        this.capacidade = capacidade;
        this.ativo = ativo;
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
                false
        );
    }

}
