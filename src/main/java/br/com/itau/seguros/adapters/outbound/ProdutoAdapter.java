package br.com.itau.seguros.adapters.outbound;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.adapters.outbound.mapper.ProdutoMapper;
import br.com.itau.seguros.adapters.outbound.repository.ProdutoRepository;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class ProdutoAdapter implements ProdutoPort {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public ProdutoDTO cadastrar(ProdutoDTO produtoDTO) {
        var produtoEntity = produtoRepository.save(ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO));
        return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(produtoEntity);
    }

    @Override
    public ProdutoDTO atualizar(ProdutoDTO produtoDTO, String categoria) {
        ProdutoEntity produtoEntity = consultar(categoria);
        if(Objects.nonNull(produtoEntity)){
            produtoEntity.setNome(produtoDTO.getNome());
            produtoEntity.setPrecoTarifado(produtoDTO.getPrecoTarifado());
            produtoEntity.setPrecoBase(produtoDTO.getPrecoBase());
            return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(produtoRepository.save(produtoEntity));
        }
        return null;
    }

    private ProdutoEntity consultar(String categoria) {
        ProdutoEntity produtoEntity = produtoRepository.findByCategoriaNome(categoria);
        if (Objects.nonNull(produtoEntity)){
            return produtoEntity;
        }
        return null;
    }

    @Override
    public Boolean existeProduto(String categoria) {
        var produtoEntity = consultar(categoria);
        if (Objects.nonNull(produtoEntity)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
