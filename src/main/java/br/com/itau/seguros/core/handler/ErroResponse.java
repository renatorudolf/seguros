package br.com.itau.seguros.core.handler;

import java.io.Serializable;

public class ErroResponse implements Serializable {

    private String codigo;
    private String descricao;

    public ErroResponse() {
    }

    public ErroResponse(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
