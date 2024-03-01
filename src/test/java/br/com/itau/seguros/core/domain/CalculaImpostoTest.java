package br.com.itau.seguros.core.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CalculaImpostoTest {

    @Test
    void deveCalcularPrecoTarifadoComSucessoNoSeguroVidaTest() {
        var calculaImposto = new CalculaImposto(Categoria.VIDA, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("103.20"), calculaImposto.calcular());
    }

    @Test
    void deveCalcularPrecoTarifadoComSucessoNoSeguroAutoTest() {
        var calculaImposto = new CalculaImposto(Categoria.AUTO, new BigDecimal("50.00"));
        assertEquals(new BigDecimal("55.25"), calculaImposto.calcular());
    }

    @Test
    void deveCalcularPrecoTarifadoComSucessoNoSeguroViagemTest() {
        var calculaImposto = new CalculaImposto(Categoria.VIAGEM, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("107.00"), calculaImposto.calcular());
    }

    @Test
    void deveCalcularPrecoTarifadoComSucessoNoSeguroResidencialTest() {
        var calculaImposto = new CalculaImposto(Categoria.RESIDENCIAL, new BigDecimal("95.11"));
        assertEquals(new BigDecimal("101.76"), calculaImposto.calcular());
    }

    @Test
    void deveCalcularPrecoTarifadoComSucessoNoSeguroPatrimonialTest() {
        var calculaImposto = new CalculaImposto(Categoria.PATRIMONIAL, new BigDecimal("35.30"));
        assertEquals(new BigDecimal("38.12"), calculaImposto.calcular());
    }
}