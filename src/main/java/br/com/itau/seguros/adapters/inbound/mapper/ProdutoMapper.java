package br.com.itau.seguros.adapters.inbound.mapper;

import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.domain.ProdutoRequest;
import br.com.itau.seguros.core.domain.ProdutoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);


    @Mapping(source = "categoria", target = "categoria", qualifiedByName = "obterCategoria")
    ProdutoDTO produtoRequestToProdutoDto(ProdutoRequest produtoRequest);

    ProdutoResponse produtoDtoToProdutoResponse(ProdutoDTO produtoDTO);

    @Named("obterCategoria")
    default Categoria getCategoria(String nomeCategoria) {
        return Objects.nonNull(nomeCategoria) ? Categoria.valueOf(nomeCategoria) : null;
    }
}
