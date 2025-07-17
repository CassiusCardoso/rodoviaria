package br.com.rodoviaria.spring_clean_arch.infrastructure.persistence.postgres.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="linhas", uniqueConstraints={
        @UniqueConstraint(columnNames = {"origem", "destino"})
})
public class LinhaModel {
    @Id
    // --- ADICIONE ESTA ANOTAÇÃO ---
    @GeneratedValue(strategy = GenerationType.UUID) // Diz ao BD para gerar o UUID
    private UUID id;
    private String origem;
    private String destino;
    private int duracaoPrevistaMinutos;
    private Boolean ativo;
    private LocalDateTime criadoEm;

    // RELACIONAMENTO INVERSO: Uma linha pode estar em muitas viagens.
    // "mappedBy" indica que a entidade ViagemModel é a dona do relacionamento.
    @OneToMany(mappedBy = "linha")
    private List<ViagemModel> viagens;

    public LinhaModel(){}
    public LinhaModel(UUID id, String origem, String destino, int duracaoPrevistaMinutos, Boolean ativo, LocalDateTime criadoEm) {
        this.id = id;
        this.origem = origem != null ? origem.trim().toUpperCase() : null;
        this.destino = destino != null ? destino.trim().toUpperCase() : null;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public List<ViagemModel> getViagens() {
        return viagens;
    }
    // EDIT 18:01 08/07
    // Setters para todos (menos para id e criadoEm)

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setDuracaoPrevistaMinutos(int duracaoPrevistaMinutos) {
        this.duracaoPrevistaMinutos = duracaoPrevistaMinutos;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setViagens(List<ViagemModel> viagens) {
        this.viagens = viagens;
    }
}
