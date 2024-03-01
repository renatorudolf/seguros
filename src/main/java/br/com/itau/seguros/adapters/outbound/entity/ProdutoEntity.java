package br.com.itau.seguros.adapters.outbound.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String identificador;
    private String nome;
    private String categoriaNome;
    private BigDecimal precoBase;
    private BigDecimal precoTarifado;

    public ProdutoEntity() {
    }

    public ProdutoEntity(Long id, String identificador, String nome, String categoriaNome, BigDecimal precoBase, BigDecimal precoTarifado) {
        this.id = id;
        this.identificador = identificador;
        this.nome = nome;
        this.categoriaNome = categoriaNome;
        this.precoBase = precoBase;
        this.precoTarifado = precoTarifado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public BigDecimal getPrecoTarifado() {
        return precoTarifado;
    }

    public void setPrecoTarifado(BigDecimal precoTarifado) {
        this.precoTarifado = precoTarifado;
    }
}
