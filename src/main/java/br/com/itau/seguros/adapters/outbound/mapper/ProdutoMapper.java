package br.com.itau.seguros.adapters.outbound.mapper;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.Objects;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper( ProdutoMapper.class );


    @Mapping(source = "identificador", target = "identificador")
    @Mapping(source = "categoria", target = "categoriaNome", qualifiedByName = "obterCategoriaNome")
    ProdutoEntity produtoDtoToProdutoEntity(ProdutoDTO produtoDTO);

    @Mapping(source = "categoriaNome", target = "categoria", qualifiedByName = "obterCategoria")
    ProdutoDTO produtoEntityToProdutoDto(ProdutoEntity produtoEntity);

    @Named("obterCategoriaNome")
    default String getCategoriaNome(Categoria categoria){
        return Objects.nonNull(categoria) ? categoria.name() : null;
    }

    @Named("obterCategoria")
    default Categoria getCategoria(String nomeCategoria){
        return Objects.nonNull(nomeCategoria) ? Categoria.valueOf(nomeCategoria) : null;
    }
}
