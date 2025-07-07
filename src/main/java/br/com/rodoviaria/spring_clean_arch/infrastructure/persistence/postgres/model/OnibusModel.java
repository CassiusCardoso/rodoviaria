package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="onibus")
public class OnibusModel {
    @Id
    private UUID id;
    private String placa;
    private String modelo;
    private int capacidade;
    private Boolean ativo;
    private LocalDateTime criadoEm;

    // RELACIONAMENTO INVERSO: Um ônibus pode ser usado em muitas viagens.
    @OneToMany(mappedBy = "onibus")
    private List<ViagemModel> viagens;

    public OnibusModel(){}
    public OnibusModel(UUID id, String placa, String modelo, int capacidade, Boolean ativo, LocalDateTime criadoEm) {
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

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
