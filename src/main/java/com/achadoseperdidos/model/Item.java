package com.achadoseperdidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório.")
    @Size(max = 100, message = "O nome do item deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "A descrição do item é obrigatória.")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
    @Column(nullable = false, length = 1000)
    private String descricao;

    @NotNull(message = "O tipo do registro é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRegistro tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusItem status = StatusItem.ABERTO;

    @NotNull(message = "A data da ocorrência é obrigatória.")
    @PastOrPresent(message = "A data da ocorrência não pode ser futura.")
    @Column(nullable = false)
    private LocalDate dataOcorrencia;

    @NotBlank(message = "O nome para contato é obrigatório.")
    @Size(max = 100, message = "O nome para contato deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nomeContato;

    @NotBlank(message = "O e-mail para contato é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres.")
    @Column(nullable = false, length = 254)
    private String emailContato;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @NotNull(message = "A categoria é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull(message = "O local é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @PrePersist
    private void definirDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }

    public void alterarStatus(StatusItem proximoStatus) {
        if (proximoStatus == null || !status.permiteTransicaoPara(proximoStatus)) {
            throw new IllegalStateException(
                    "Não é permitido alterar o status de " + status + " para " + proximoStatus + "."
            );
        }
        status = proximoStatus;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public void setTipo(TipoRegistro tipo) {
        this.tipo = tipo;
    }

    public StatusItem getStatus() {
        return status;
    }

    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(LocalDate dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }
}
