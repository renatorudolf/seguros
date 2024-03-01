package br.com.itau.seguros.adapters.outbound;

import br.com.itau.seguros.ProdutoUtil;
import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.adapters.outbound.mapper.ProdutoMapper;
import br.com.itau.seguros.adapters.outbound.repository.ProdutoRepository;
import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class ProdutoAdapterTest {

    @InjectMocks
    private ProdutoAdapter produtoAdapter;

    @Spy
    private ProdutoRepository produtoRepository;

    @Test
    void deveCadastrarTest() {
        ProdutoDTO produtoDTO = ProdutoUtil.getProdutoDTO();
        produtoDTO.setPrecoTarifado(new BigDecimal("103.20"));
        ProdutoEntity produtoEntity = ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO);

        Mockito.when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        ProdutoDTO produtoDTOSalvo = produtoAdapter.cadastrar(produtoDTO, any(String.class));
        assertEquals("Seguro Automotivo Silver", produtoDTOSalvo.getNome());
        assertEquals(new BigDecimal("50.00"), produtoDTOSalvo.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), produtoDTOSalvo.getPrecoTarifado());
        assertNotNull(produtoDTOSalvo.getIdentificador());
        assertEquals(Categoria.AUTO, produtoDTOSalvo.getCategoria());
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).save(any(ProdutoEntity.class));
    }

    @Test
    void deveAtualizarQuandoProdutoExisteTest() {
        ProdutoDTO produtoDTO = ProdutoUtil.getProdutoDTO();
        produtoDTO.setPrecoTarifado(new BigDecimal("103.20"));
        ProdutoEntity produtoEntity = ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO);

        Mockito.when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        Mockito.when(produtoRepository.findByCategoriaNome(produtoDTO.getCategoria().name())).thenReturn(produtoEntity);

        ProdutoDTO produtoDTOAtualizado = produtoAdapter.atualizar(produtoDTO, produtoDTO.getCategoria().name(), any(String.class));
        assertEquals("Seguro Automotivo Silver", produtoDTOAtualizado.getNome());
        assertEquals(new BigDecimal("50.00"), produtoDTOAtualizado.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), produtoDTOAtualizado.getPrecoTarifado());
        assertNotNull(produtoDTOAtualizado.getIdentificador());
        assertEquals(Categoria.AUTO, produtoDTOAtualizado.getCategoria());
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).save(any(ProdutoEntity.class));
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).findByCategoriaNome(produtoDTO.getCategoria().name());
    }

    @Test
    void deveAtualizarQuandoProdutoNaoExisteTest() {
        ProdutoDTO produtoDTO = ProdutoUtil.getProdutoDTO();
        produtoDTO.setPrecoTarifado(new BigDecimal("103.20"));
        ProdutoEntity produtoEntity = ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO);

        Mockito.when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);
        Mockito.when(produtoRepository.findByCategoriaNome(produtoDTO.getCategoria().name())).thenReturn(null);

        ProdutoDTO produtoDTONaoAtualizado = produtoAdapter.atualizar(produtoDTO, produtoDTO.getCategoria().name(), any(String.class));
        assertNull(produtoDTONaoAtualizado);
        Mockito.verify(produtoRepository, Mockito.never()).save(any(ProdutoEntity.class));
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).findByCategoriaNome(produtoDTO.getCategoria().name());
    }

    @Test
    void deveRetornarTrueQuandoProdutoExisteTest() {
        ProdutoDTO produtoDTO = ProdutoUtil.getProdutoDTO();
        produtoDTO.setPrecoTarifado(new BigDecimal("103.20"));
        ProdutoEntity produtoEntity = ProdutoMapper.INSTANCE.produtoDtoToProdutoEntity(produtoDTO);
        Mockito.when(produtoRepository.findByCategoriaNome(produtoDTO.getCategoria().name())).thenReturn(produtoEntity);
        assertTrue(produtoAdapter.existeProduto(produtoDTO.getCategoria().name()));
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).findByCategoriaNome(produtoDTO.getCategoria().name());
    }

    @Test
    void deveRetornarFalseQuandoProdutoNaoExisteTest() {
        ProdutoDTO produtoDTO = ProdutoUtil.getProdutoDTO();
        produtoDTO.setPrecoTarifado(new BigDecimal("103.20"));
        Mockito.when(produtoRepository.findByCategoriaNome(produtoDTO.getCategoria().name())).thenReturn(null);
        assertFalse(produtoAdapter.existeProduto(produtoDTO.getCategoria().name()));
        Mockito.verify(produtoRepository, Mockito.atLeastOnce()).findByCategoriaNome(produtoDTO.getCategoria().name());
    }
}