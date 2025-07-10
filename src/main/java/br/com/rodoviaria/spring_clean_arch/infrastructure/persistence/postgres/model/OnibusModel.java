package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="onibus")
public class OnibusModel {
    @Id
    // --- ADICIONE ESTA ANOTAÇÃO ---
    @GeneratedValue(strategy = GenerationType.UUID) // Diz ao BD para gerar o UUID
    private UUID id;
    private String placa;
    private String modelo;
    private int capacidade;
    private Boolean ativo;
    // CORREÇÃO: Remova o set e adicione a anotação
    @CreationTimestamp // Diz ao Hibernate para preencher este campo na criação
    @Column(updatable = false) // Torna a coluna imutável em atualizações
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

    // EDIT 18:01 08/07
    // Setters para todos (menos para id e criadoEm)

    public void setViagens(List<ViagemModel> viagens) {
        this.viagens = viagens;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
