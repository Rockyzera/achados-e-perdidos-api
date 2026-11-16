package com.achadoseperdidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "locais")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do local é obrigatório.")
    @Size(max = 100, message = "O nome do local deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 250, message = "Os detalhes devem ter no máximo 250 caracteres.")
    @Column(length = 250)
    private String detalhes;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "A sigla do estado é obrigatória.")
    @Size(min = 2, max = 2, message = "Informe a sigla do estado com duas letras.")
    @Column(nullable = false, length = 2)
    private String estado;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
