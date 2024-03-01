package br.com.itau.seguros.core.service;

import br.com.itau.seguros.core.domain.CalculaImposto;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.ports.in.CalculaTarifaServicePort;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CalculaTarifaService implements CalculaTarifaServicePort {

    Logger logger = LoggerFactory.getLogger(CalculaTarifaService.class);

    @Autowired
    private ProdutoPort produtoPort;

    @Override
    public ProdutoDTO cadastrarSeguro(ProdutoDTO produtoDTO) {
        logger.info("Verificando se o seguro ja existe na base");
        if (produtoPort.existeProduto(produtoDTO.getCategoria().name()).equals(Boolean.FALSE)){
            produtoDTO.setIdentificador(getIdentificador());
            return produtoPort.cadastrar(calcularImposto(produtoDTO));
        }
        return null;
    }

    private ProdutoDTO calcularImposto(ProdutoDTO produtoDTO) {
        logger.info("Calculando preco tarifado");
        var calculaImposto = new CalculaImposto(produtoDTO.getCategoria(), produtoDTO.getPrecoBase());
        BigDecimal precoTarifado = calculaImposto.calcular();
        produtoDTO.setPrecoTarifado(precoTarifado);
        logger.info("Preco tarifado calculado com sucesso");
        return produtoDTO;
    }

    @Override
    public ProdutoDTO atualizarCalculoSeguro(ProdutoDTO produtoDTO, String categoria) {
        logger.info("Verificando se o seguro ja existe na base");
        if (produtoPort.existeProduto(categoria).equals(Boolean.TRUE)){
            ProdutoDTO produtoCalculado = calcularImposto(produtoDTO);
            return produtoPort.atualizar(produtoCalculado, categoria);
        }
        return null;
    }
    private String getIdentificador(){
        return UUID.randomUUID().toString();
    }
}
