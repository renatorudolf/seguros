package br.com.itau.seguros.core.service;

import br.com.itau.seguros.core.domain.CalculaImposto;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.logger.LogHelper;
import br.com.itau.seguros.ports.in.CalculaTarifaServicePort;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Level;

@Component
public class CalculaTarifaService implements CalculaTarifaServicePort {

    @Autowired
    private ProdutoPort produtoPort;

    @Override
    public ProdutoDTO cadastrarSeguro(ProdutoDTO produtoDTO, String correlationId) {
        LogHelper.printLog(Level.INFO, "Verificando se o seguro ja existe na base", correlationId, CalculaTarifaService.class);
        if (produtoPort.existeProduto(produtoDTO.getCategoria().name()).equals(Boolean.FALSE)){
            produtoDTO.setIdentificador(getIdentificador());
            return produtoPort.cadastrar(calcularImposto(produtoDTO), correlationId);
        }
        return null;
    }

    private ProdutoDTO calcularImposto(ProdutoDTO produtoDTO) {
        LogHelper.printLog(Level.INFO, "Calculando preco tarifado", CalculaTarifaService.class);
        var calculaImposto = new CalculaImposto(produtoDTO.getCategoria(), produtoDTO.getPrecoBase());
        BigDecimal precoTarifado = calculaImposto.calcular();
        produtoDTO.setPrecoTarifado(precoTarifado);
        LogHelper.printLog(Level.INFO, "Preco tarifado calculado com sucesso", CalculaTarifaService.class);
        return produtoDTO;
    }

    @Override
    public ProdutoDTO atualizarCalculoSeguro(ProdutoDTO produtoDTO, String categoria, String correlationId) {
        LogHelper.printLog(Level.INFO, "Verificando se o seguro ja existe na base", CalculaTarifaService.class);
        if (produtoPort.existeProduto(categoria).equals(Boolean.TRUE)){
            ProdutoDTO produtoCalculado = calcularImposto(produtoDTO);
            return produtoPort.atualizar(produtoCalculado, categoria, correlationId);
        }
        return null;
    }
    private String getIdentificador(){
        return UUID.randomUUID().toString();
    }
}
