package br.com.itau.seguros.ports.out;

import br.com.itau.seguros.core.domain.ProdutoDTO;

public interface ProdutoPort {

    ProdutoDTO cadastrar(ProdutoDTO produtoDTO, String correlationId);
    ProdutoDTO atualizar(ProdutoDTO produtoDTO, String identificador, String correlationId);
    Boolean existeProduto(String identificador);
}
