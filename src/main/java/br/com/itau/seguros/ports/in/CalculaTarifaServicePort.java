package br.com.itau.seguros.ports.in;

import br.com.itau.seguros.core.domain.ProdutoDTO;

public interface CalculaTarifaServicePort {

    ProdutoDTO cadastrarSeguro(ProdutoDTO produtoDTO, String correlationId);
    ProdutoDTO atualizarCalculoSeguro(ProdutoDTO produtoDTO, String categoria, String correlationId);
}
