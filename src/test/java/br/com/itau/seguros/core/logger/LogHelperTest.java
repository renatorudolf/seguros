package br.com.itau.seguros.core.logger;

import br.com.itau.seguros.ProdutoUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogHelperTest {

    @Test
    void deveConverteObjetoEmJsonQuandoObjetoDiferenteDeNuloTest(){
        var produtoJson = LogHelper.convertObjectToJson(ProdutoUtil.getProdutoRequest());
        assertNotNull(produtoJson);
        assertTrue(produtoJson.contains("VIDA"));
    }

    @Test
    void deveConverteObjetoEmJsonQuandoObjetoENuloTest(){
        var produtoJson = LogHelper.convertObjectToJson(null);
        assertNull(produtoJson);
    }
}