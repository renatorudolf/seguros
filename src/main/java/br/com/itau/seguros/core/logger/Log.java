package br.com.itau.seguros.core.logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Log implements Serializable {

    private String canal;
    private String descricao;
    private String correlationId;
    private String data;

    public Log() {
    }

    public Log(String canal, String descricao, String correlationId, String data) {
        this.canal = canal;
        this.descricao = descricao;
        this.correlationId = correlationId;
        this.data = data;
    }

    public Log(String canal, String descricao) {
        this.canal = canal;
        this.descricao = descricao;
    }

    public Log(String canal, String descricao, String correlationId) {
        this.canal = canal;
        this.descricao = descricao;
        this.correlationId = correlationId;
    }

    public String getCanal() {
        return canal;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getData() {
        return data;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
