package br.com.itau.seguros.adapters.outbound;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.adapters.outbound.mapper.ProdutoMapper;
import br.com.itau.seguros.adapters.outbound.repository.ProdutoRepository;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.logger.LogHelper;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.logging.Level;

@Component
public class ProdutoAdapter implements ProdutoPort {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public ProdutoDTO cadastrar(ProdutoDTO produtoDTO, String correlationId) {
        LogHelper.printLog(Level.INFO, "Cadastrando seguro", correlationId, ProdutoAdapter.class);
        var produtoEntity = produtoRepository.save(ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO));
        LogHelper.printLog(Level.INFO, "Seguro cadastrado com sucesso", correlationId, ProdutoAdapter.class);
        return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(produtoEntity);
    }

    @Override
    public ProdutoDTO atualizar(ProdutoDTO produtoDTO, String categoria, String correlationId) {
        LogHelper.printLog(Level.INFO, "Atualizando seguro", correlationId, ProdutoAdapter.class);
        ProdutoEntity produtoEntity = consultar(categoria);
        if(Objects.nonNull(produtoEntity)){
            produtoEntity.setNome(produtoDTO.getNome());
            produtoEntity.setPrecoTarifado(produtoDTO.getPrecoTarifado());
            produtoEntity.setPrecoBase(produtoDTO.getPrecoBase());
            ProdutoEntity entitySalvo = produtoRepository.save(produtoEntity);
            LogHelper.printLog(Level.INFO, "Seguro atualizado com sucesso", correlationId, ProdutoAdapter.class);
            return ProdutoMapper.INSTANCE.produtoEntityToProdutoDto(entitySalvo);
        }
        LogHelper.printLog(Level.INFO, "Seguro a ser atualizado nao existe na base de dados", correlationId, ProdutoAdapter.class);
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
        LogHelper.printLog(Level.INFO, "Verificando existencia do Seguro", ProdutoAdapter.class);
        var produtoEntity = consultar(categoria);
        if (Objects.nonNull(produtoEntity)){
            LogHelper.printLog(Level.INFO, "Seguro encontrado", ProdutoAdapter.class);
            return Boolean.TRUE;
        }
        LogHelper.printLog(Level.INFO, "Seguro nao existe", ProdutoAdapter.class);
        return Boolean.FALSE;
    }
}
