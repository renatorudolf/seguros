package br.com.itau.seguros.core.domain;

import java.math.BigDecimal;

public enum Categoria {

    VIDA(new BigDecimal("1"), new BigDecimal("2.2"), null),
    AUTO(new BigDecimal("5.5"), new BigDecimal("4"), new BigDecimal("1")),
    VIAGEM(new BigDecimal("2"), new BigDecimal("4"), new BigDecimal("1")),
    RESIDENCIAL(new BigDecimal("4"), null, new BigDecimal("3")),
    PATRIMONIAL(new BigDecimal("5"), new BigDecimal("3"), null);

    private BigDecimal iof;
    private BigDecimal pis;
    private BigDecimal cofins;

    Categoria(BigDecimal iof, BigDecimal pis, BigDecimal cofins) {
        this.iof = iof;
        this.pis = pis;
        this.cofins = cofins;
    }

    public BigDecimal getIof() {
        return iof;
    }

    public BigDecimal getPis() {
        return pis;
    }

    public BigDecimal getCofins() {
        return cofins;
    }
}
