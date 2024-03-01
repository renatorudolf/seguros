package br.com.itau.seguros.core.service;

import br.com.itau.seguros.ProdutoUtil;
import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.ports.out.ProdutoPort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.UUID;

import static br.com.itau.seguros.ProdutoUtil.getProdutoDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CalculaTarifaServiceTest {

    @InjectMocks
    private CalculaTarifaService calculaTarifaService;

    @Mock
    private ProdutoPort produtoPort;

    @Test
    void deveCalcularSeguroParaProdutoNaoRegistradoTest() {
        var correlationId = UUID.randomUUID().toString();
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(produtoPort.existeProduto(Categoria.VIDA.name())).thenReturn(Boolean.FALSE);
        Mockito.when(produtoPort.cadastrar(produtoDTO, correlationId)).thenReturn(produtoDTO);

        var seguroCalculado = calculaTarifaService.cadastrarSeguro(produtoDTO, correlationId);
        assertEquals("Seguro de Vida Individual", seguroCalculado.getNome());
        assertEquals(new BigDecimal("100.00"), seguroCalculado.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), seguroCalculado.getPrecoTarifado());
        assertNotNull(seguroCalculado.getIdentificador());
        assertEquals(Categoria.VIDA, seguroCalculado.getCategoria());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).cadastrar(produtoDTO, correlationId);
    }

    @Test
    void deveRetornarNuloParaProdutoJaRegistradoTest() {
        var correlationId = UUID.randomUUID().toString();
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(produtoPort.existeProduto(Categoria.VIDA.name())).thenReturn(Boolean.TRUE);
        Mockito.when(produtoPort.cadastrar(produtoDTO, correlationId)).thenReturn(produtoDTO);
        assertNull(calculaTarifaService.cadastrarSeguro(produtoDTO, correlationId));
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.never()).cadastrar(produtoDTO, correlationId);
    }

    @Test
    void deveAtualizarCalculoParaProdutoRegistradoTest() {
        var produtoDTO = getProdutoDTO();
        var correlationId = UUID.randomUUID().toString();
        Mockito.when(produtoPort.existeProduto(Categoria.AUTO.name())).thenReturn(Boolean.TRUE);
        Mockito.when(produtoPort.atualizar(produtoDTO, produtoDTO.getCategoria().name(), correlationId)).thenReturn(produtoDTO);

        var seguroCalculado = calculaTarifaService.atualizarCalculoSeguro(produtoDTO, produtoDTO.getCategoria().name(), correlationId);
        assertEquals("Seguro Automotivo Silver", seguroCalculado.getNome());
        assertEquals(new BigDecimal("50.00"), seguroCalculado.getPrecoBase());
        assertEquals(new BigDecimal("55.25"), seguroCalculado.getPrecoTarifado());
        assertNotNull(seguroCalculado.getIdentificador());
        assertEquals(Categoria.AUTO, seguroCalculado.getCategoria());
        Mockito.verify(produtoPort, Mockito.never()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.atLeastOnce()).atualizar(produtoDTO, produtoDTO.getCategoria().name(), correlationId);
    }

    @Test
    void deveRetornarNuloParaProdutoNaoRegistradoTest() {
        var produtoDTO = getProdutoDTO();
        var correlationId = UUID.randomUUID().toString();
        Mockito.when(produtoPort.existeProduto(Categoria.AUTO.name())).thenReturn(Boolean.FALSE);
        Mockito.when(produtoPort.atualizar(produtoDTO, produtoDTO.getCategoria().name(), correlationId)).thenReturn(produtoDTO);
        assertNull(calculaTarifaService.atualizarCalculoSeguro(produtoDTO, produtoDTO.getCategoria().name(), correlationId));
        Mockito.verify(produtoPort, Mockito.never()).existeProduto(Categoria.VIDA.name());
        Mockito.verify(produtoPort, Mockito.never()).atualizar(produtoDTO, produtoDTO.getCategoria().name(), correlationId);
    }
}