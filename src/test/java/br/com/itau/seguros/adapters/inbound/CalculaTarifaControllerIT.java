package br.com.itau.seguros.adapters.inbound;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import br.com.itau.seguros.adapters.outbound.repository.ProdutoRepository;
import br.com.itau.seguros.core.domain.Categoria;
import br.com.itau.seguros.core.domain.ProdutoRequest;
import br.com.itau.seguros.core.handler.ErroResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CalculaTarifaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository repository;

    @AfterEach
    void down(){
        repository.deleteAll();
    }

    @Test
    @DisplayName("Salvar um novo produto com preco tarifado")
    void salvarNovoProdutoComValorTarifado() throws Exception {
        var produtoRequest = getProdutoRequest();
        String payload = new ObjectMapper().writeValueAsString(produtoRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/seguro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Seguro de Vida Individual"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria").value(Categoria.VIDA.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco_base").value(new BigDecimal("100.0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco_tarifado").value(new BigDecimal("103.2")));


        var produtoEntity = repository.findByCategoriaNome(produtoRequest.getCategoria().name());
        assertNotNull(produtoEntity.getIdentificador());
        assertEquals("Seguro de Vida Individual", produtoEntity.getNome());
        assertEquals(new BigDecimal("100.00"), produtoEntity.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), produtoEntity.getPrecoTarifado());
    }

    @Test
    @DisplayName("Salvar em duplicidade produto com preco tarifado")
    void salvarEmDuplicidadeProdutoComValorTarifado() throws Exception {
        repository.save(getProdutoEntity());
        var produtoRequest = getProdutoRequest();
        String payload = new ObjectMapper().writeValueAsString(produtoRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/seguro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("422"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Recurso existente na base"));


        var quantidadeDeRegistro = repository.findAll().size();
        assertEquals(1, quantidadeDeRegistro);
    }

    @Test
    @DisplayName("Atualizar novo produto com preco tarifado")
    void atualizarProdutoComValorTarifado() throws Exception {
        var produtoRequest = getProdutoRequest();
        produtoRequest.setNome("Seguro de Vida Plus");
        produtoRequest.setPrecoBase(new BigDecimal("150.00"));

        repository.save(getProdutoEntity());

        String payload = new ObjectMapper().writeValueAsString(produtoRequest);
        mockMvc.perform(MockMvcRequestBuilders.patch("/seguro/"+produtoRequest.getCategoria() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Seguro de Vida Plus"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoria").value(Categoria.VIDA.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco_base").value(new BigDecimal("150.0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.preco_tarifado").value(new BigDecimal("154.8")));


        var produtoEntity = repository.findByCategoriaNome(produtoRequest.getCategoria().name());
        assertNotNull(produtoEntity.getIdentificador());
        assertEquals("Seguro de Vida Plus", produtoEntity.getNome());
        assertEquals(new BigDecimal("150.00"), produtoEntity.getPrecoBase());
        assertEquals(new BigDecimal("154.80"), produtoEntity.getPrecoTarifado());
    }

    @Test
    @DisplayName("Atualizar produto que não existe")
    void atualizarProdutoQueNaoExiste() throws Exception {
        var produtoRequest = getProdutoRequest();
        produtoRequest.setCategoria(Categoria.AUTO);
        produtoRequest.setPrecoBase(new BigDecimal("150.00"));

        repository.save(getProdutoEntity());

        String payload = new ObjectMapper().writeValueAsString(produtoRequest);
        mockMvc.perform(MockMvcRequestBuilders.patch("/seguro/"+produtoRequest.getCategoria() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.descricao").value("Recurso não encontrado para atualizar"));


        var produtoEntity = repository.findByCategoriaNome(Categoria.VIDA.name());
        assertNotNull(produtoEntity.getIdentificador());
        assertEquals("Seguro de Vida Individual", produtoEntity.getNome());
        assertEquals(new BigDecimal("100.00"), produtoEntity.getPrecoBase());
        assertEquals(new BigDecimal("103.20"), produtoEntity.getPrecoTarifado());
    }

    private ProdutoRequest getProdutoRequest(){
        var request = new ProdutoRequest();
        request.setNome("Seguro de Vida Individual");
        request.setCategoria(Categoria.VIDA);
        request.setPrecoBase(new BigDecimal("100.00"));
        return request;
    }

    private ProdutoEntity getProdutoEntity(){
        var entity = new ProdutoEntity();
        entity.setNome("Seguro de Vida Individual");
        entity.setIdentificador(UUID.randomUUID().toString());
        entity.setCategoriaNome(Categoria.VIDA.name());
        entity.setPrecoBase(new BigDecimal("100.00"));
        entity.setPrecoTarifado(new BigDecimal("103.20"));
        return entity;
    }
}


