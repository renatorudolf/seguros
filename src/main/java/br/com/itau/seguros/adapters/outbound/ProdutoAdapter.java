package br.com.itau.seguros.adapters.outbound;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.adapters.outbound.mapper.ProdutoMapper;
import br.com.itau.seguros.adapters.outbound.repository.ProdutoRepository;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class ProdutoAdapter implements ProdutoPort {

    Logger logger = LoggerFactory.getLogger(ProdutoAdapter.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public ProdutoDTO cadastrar(ProdutoDTO produtoDTO) {
        logger.info("Cadastrando seguro");
        var produtoEntity = produtoRepository.save(ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO));
        logger.info("Seguro cadastrado com sucesso");
        return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(produtoEntity);
    }

    @Override
    public ProdutoDTO atualizar(ProdutoDTO produtoDTO, String categoria) {
        logger.info("Atualizando seguro");
        ProdutoEntity produtoEntity = consultar(categoria);
        if(Objects.nonNull(produtoEntity)){
            produtoEntity.setNome(produtoDTO.getNome());
            produtoEntity.setPrecoTarifado(produtoDTO.getPrecoTarifado());
            produtoEntity.setPrecoBase(produtoDTO.getPrecoBase());
            ProdutoEntity entitySalvo = produtoRepository.save(produtoEntity);
            logger.info("Seguro atualizado com sucesso");
            return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(entitySalvo);
        }
        logger.info("Seguro a ser atualizado nao existe na base de dados");
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
        logger.info("Verificando existencia do Seguro");
        var produtoEntity = consultar(categoria);
        if (Objects.nonNull(produtoEntity)){
            logger.info("Seguro encontrado");
            return Boolean.TRUE;
        }
        logger.info("Seguro nao existe");
        return Boolean.FALSE;
    }
}
