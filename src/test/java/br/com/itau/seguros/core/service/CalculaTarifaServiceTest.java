package br.com.itau.seguros.core.service;

import br.com.itau.seguros.ProdutoUtil;
import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static br.com.itau.seguros.ProdutoUtil.getProdutoDTO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculaTarifaServiceTest {

    @InjectMocks
    private CalculaTarifaService calculaTarifaService;

    @Mock
    private ProdutoPort produtoPort;

    @Test
    void deveCalcularSeguroParaProdutoNaoRegistradoTest() {
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(produtoPort.existeProduto(Categoria.VIDA.name())).thenReturn(Boolean.FALSE);
        Mockito.when(produtoPort.cadastrar(produtoDTO)).thenReturn(produtoDTO);

        var seguroCalculado = calculaTarifaService.cadastrarSeguro(produtoDTO);
        assertEquals("Seguro de Vida Individual", seguroCalculado.getNome());
        assertEquals(new BigDecimal("100.00"), seguroCalculado.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), seguroCalculado.getPrecoTarifado());
        assertNotNull(seguroCalculado.getIdentificador());
        assertEquals(Categoria.VIDA, seguroCalculado.getCategoria());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).cadastrar(produtoDTO);
    }

    @Test
    void deveRetornarNuloParaProdutoJaRegistradoTest() {
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(produtoPort.existeProduto(Categoria.VIDA.name())).thenReturn(Boolean.TRUE);
        Mockito.when(produtoPort.cadastrar(produtoDTO)).thenReturn(produtoDTO);
        assertNull(calculaTarifaService.cadastrarSeguro(produtoDTO));
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.never()).cadastrar(produtoDTO);
    }

    @Test
    void deveAtualizarCalculoParaProdutoRegistradoTest() {
        var produtoDTO = getProdutoDTO();

        Mockito.when(produtoPort.existeProduto(Categoria.AUTO.name())).thenReturn(Boolean.TRUE);
        Mockito.when(produtoPort.atualizar(produtoDTO, produtoDTO.getCategoria().name())).thenReturn(produtoDTO);

        var seguroCalculado = calculaTarifaService.atualizarCalculoSeguro(produtoDTO, produtoDTO.getCategoria().name());
        assertEquals("Seguro Automotivo Silver", seguroCalculado.getNome());
        assertEquals(new BigDecimal("50.00"), seguroCalculado.getPrecoBase());
        assertEquals(new BigDecimal("55.25"), seguroCalculado.getPrecoTarifado());
        assertNotNull(seguroCalculado.getIdentificador());
        assertEquals(Categoria.AUTO, seguroCalculado.getCategoria());
        Mockito.verify(produtoPort, Mockito.never()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).atualizar(produtoDTO, produtoDTO.getCategoria().name());
    }

    @Test
    void deveRetornarNuloParaProdutoNaoRegistradoTest() {
        var produtoDTO = getProdutoDTO();
        Mockito.when(produtoPort.existeProduto(Categoria.AUTO.name())).thenReturn(Boolean.FALSE);
        Mockito.when(produtoPort.atualizar(produtoDTO, produtoDTO.getCategoria().name())).thenReturn(produtoDTO);
        assertNull(calculaTarifaService.atualizarCalculoSeguro(produtoDTO, produtoDTO.getCategoria().name()));
        Mockito.verify(produtoPort, Mockito.never()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.never()).atualizar(produtoDTO, produtoDTO.getCategoria().name());
    }
}