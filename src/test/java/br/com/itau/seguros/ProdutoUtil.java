package br.com.itau.seguros;

import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.domain.ProdutoRequest;
import java.math.BigDecimal;
import java.util.UUID;

public class ProdutoUtil {

    public static ProdutoRequest getProdutoRequest(){
        ProdutoRequest request = new ProdutoRequest();
        request.setCategoria(Categoria.VIDA);
        request.setNome("Seguro de Vida Individual");
        request.setPrecoBase(new BigDecimal("100.00"));
        return request;
    }

    public static ProdutoDTO getProdutoDTO(){
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Seguro Automotivo Silver");
        produtoDTO.setCategoria(Categoria.AUTO);
        produtoDTO.setPrecoBase(new BigDecimal("50.00"));
        produtoDTO.setIdentificador(UUID.randomUUID().toString());
        return produtoDTO;
    }
}
