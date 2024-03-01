package br.com.itau.seguros.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.math.BigDecimal;

public class ProdutoRequest {

    @NotNull(message = "Campo 'nome' nao pode ser nulo")
    @NotEmpty(message = "Campo 'nome' nao pode ser branco")
    @Length(min = 5, message = "Campo 'nome' deve ter pelo menos 5 digitos")
    private String nome;
    @NotNull(message = "Campo 'categoria' nao pode ser nulo")
    private Categoria categoria;
    @NotNull(message = "Campo 'preco_base' nao pode ser nulo")
    @Digits(integer=5, fraction = 2, message = "Campo 'preco_base' deve ter 5 inteiros e 2 decimais")
    @JsonProperty("preco_base")
    private BigDecimal precoBase;

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
}
