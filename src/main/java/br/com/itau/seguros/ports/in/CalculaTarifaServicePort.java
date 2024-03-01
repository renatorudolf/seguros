package br.com.itau.seguros.ports.in;

import br.com.itau.seguros.core.domain.ProdutoDTO;

public interface CalculaTarifaServicePort {

    ProdutoDTO calcularSeguro(ProdutoDTO produtoDTO);
    ProdutoDTO atualizarCalculoSeguro(ProdutoDTO produtoDTO, String categoria);
}