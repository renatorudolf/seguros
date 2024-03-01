package br.com.itau.seguros.ports.out;

import br.com.itau.seguros.core.domain.ProdutoDTO;

public interface ProdutoPort {

    ProdutoDTO cadastrar(ProdutoDTO produtoDTO);
    ProdutoDTO atualizar(ProdutoDTO produtoDTO, String identificador);
    Boolean existeProduto(String identificador);
}
