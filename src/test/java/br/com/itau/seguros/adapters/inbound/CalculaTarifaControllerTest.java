package br.com.itau.seguros.adapters.inbound;

import br.com.itau.seguros.ProdutoUtil;
import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.ports.in.CalculaTarifaServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculaTarifaController.class)
class CalculaTarifaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculaTarifaServicePort calculaTarifaService;

    @Test
    void deveCalcularImpostoEGravarNaBaseComSucessoTest() throws Exception {
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(calculaTarifaService.cadastrarSeguro(any(ProdutoDTO.class))).thenReturn(produtoDTO);
        String requestBody = objectMapper.writeValueAsString(ProdutoUtil.getProdutoRequest());
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.atLeastOnce()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarExcecaoAoGravarProdutoJaCadastradoTest() throws Exception {
        Mockito.when(calculaTarifaService.cadastrarSeguro(any(ProdutoDTO.class))).thenReturn(null);
        String requestBody = objectMapper.writeValueAsString(ProdutoUtil.getProdutoRequest());
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.atLeastOnce()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComNomeNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setNome(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComNomeBrancoTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setNome("");
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComCategoriaNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setCategoria(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComPrecoBaseNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComPrecoBaseComMaisDeCincoDigitosTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(new BigDecimal("100000"));
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoGravarProdutoComPrecoBaseComMaisDeDuasCasasDecimaisTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(new BigDecimal("100.876"));
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(post("/seguro").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveAtualizarImpostoEGravarNaBaseComSucessoTest() throws Exception {
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(calculaTarifaService.atualizarCalculoSeguro(any(ProdutoDTO.class), any(String.class))).thenReturn(produtoDTO);
        String requestBody = objectMapper.writeValueAsString(ProdutoUtil.getProdutoRequest());
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.atLeastOnce()).atualizarCalculoSeguro(any(ProdutoDTO.class), any(String.class));
    }

    @Test
    void deveLancarBadRequestAoAtualizarProdutoComNomeNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setNome(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }


    @Test
    void deveLancarBadRequestAoAtualizarProdutoComNomeEmBrancoTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setNome("");
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }


    @Test
    void deveLancarBadRequestAoAtualizarProdutoComCategoriaNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setCategoria(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoAtualizarProdutoComPrecoBaseComMaisDeCincoDigitosTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(new BigDecimal("100000"));
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoAtualizarProdutoComPrecoBaseComMaisDeDuasCasasDecimaisTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(new BigDecimal("100.564"));
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarBadRequestAoAtualizarProdutoComPrecoBaseNuloTest() throws Exception {
        var produtoRequest = ProdutoUtil.getProdutoRequest();
        produtoRequest.setPrecoBase(null);
        String requestBody = objectMapper.writeValueAsString(produtoRequest);
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.never()).cadastrarSeguro(any(ProdutoDTO.class));
    }

    @Test
    void deveLancarExcecaoParaProdutoNaoEncontradoNaBaseTest() throws Exception {
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(ProdutoUtil.getProdutoRequest());
        Mockito.when(calculaTarifaService.atualizarCalculoSeguro(any(ProdutoDTO.class), any(String.class))).thenReturn(null);
        String requestBody = objectMapper.writeValueAsString(ProdutoUtil.getProdutoRequest());
        mockMvc.perform(patch("/seguro/VIDA").contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andDo(print());
        Mockito.verify(calculaTarifaService, Mockito.atLeastOnce()).atualizarCalculoSeguro(any(ProdutoDTO.class), any(String.class));
    }
}