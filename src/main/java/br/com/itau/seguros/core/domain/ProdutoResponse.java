package br.com.itau.seguros.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class ProdutoResponse {

    @JsonProperty("id")
    private String identificador;
    private String nome;
    private Categoria categoria;
    @JsonProperty("preco_base")
    private BigDecimal precoBase;
    @JsonProperty("preco_tarifado")
    private BigDecimal precoTarifado;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
