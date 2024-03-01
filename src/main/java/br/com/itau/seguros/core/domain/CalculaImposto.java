package br.com.itau.seguros.core.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CalculaImposto {

    private Categoria categoria;
    private BigDecimal precoBase;

    public CalculaImposto(Categoria categoria, BigDecimal precoBase) {
        this.categoria = categoria;
        this.precoBase = precoBase;
    }

    public BigDecimal calcular() {
        BigDecimal precoBaseComIof = calcularIof();
        BigDecimal precoBaseComPis = calcularPis();
        BigDecimal precoBaseComCofins = calcularCofins();
        return precoBase.add(precoBaseComIof).add(precoBaseComPis).add(precoBaseComCofins);
    }

    private BigDecimal calcularIof() {
        return Objects.nonNull(categoria.getIof()) ? precoBase.multiply(categoria.getIof().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
    }

    private BigDecimal calcularPis() {
        return Objects.nonNull(categoria.getPis()) ? precoBase.multiply(categoria.getPis().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
    }

    private BigDecimal calcularCofins() {
        return Objects.nonNull(categoria.getCofins()) ? precoBase.multiply(categoria.getCofins().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO;
    }
}
