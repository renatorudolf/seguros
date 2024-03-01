package br.com.itau.seguros.adapters.inbound;

import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.domain.ProdutoRequest;
import br.com.itau.seguros.core.domain.ProdutoResponse;
import br.com.itau.seguros.core.logger.LogHelper;
import br.com.itau.seguros.ports.in.CalculaTarifaServicePort;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.text.MessageFormat;
import java.util.Objects;

@RestController
@RequestMapping("/seguro")
public class CalculaTarifaController {

    Logger logger = LoggerFactory.getLogger(CalculaTarifaController.class);

    @Autowired
    private CalculaTarifaServicePort calculaTarifaService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> calcularImposto(@RequestBody @Valid ProdutoRequest payload) {
        logger.info(MessageFormat.format("Payload de requisicao recebido: {0}", LogHelper.convertObjectToJson(payload)));
        logger.info("Convertendo requisicao em objeto de dominio");
        ProdutoDTO produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        logger.info(MessageFormat.format("Convertido com sucesso requisicao em objeto de dominio: {0}", LogHelper.convertObjectToJson(produtoDTO)));
        ProdutoDTO seguroCalculado = calculaTarifaService.cadastrarSeguro(produtoDTO);
        if (Objects.nonNull(seguroCalculado)) {
            logger.info(MessageFormat.format("Seguro calculado com sucesso: {0}", LogHelper.convertObjectToJson(seguroCalculado)));
            return new ResponseEntity<>(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado), HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Recurso existente na base");
        }

    @PatchMapping("/{categoria}")
    public ResponseEntity<ProdutoResponse> atualizarCalculoImposto(@RequestBody @Valid ProdutoRequest payload, @PathVariable("categoria") String categoria){
        logger.info(MessageFormat.format("Payload de requisicao recebido: {0} com parametro de atualizacao: {1}", LogHelper.convertObjectToJson(payload), categoria));
        ProdutoDTO produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        logger.info("Convertendo requisicao em objeto de dominio");
        ProdutoDTO seguroCalculado = calculaTarifaService.atualizarCalculoSeguro(produtoDTO, categoria);
        logger.info(MessageFormat.format("Convertido com sucesso requisicao em objeto de dominio: {0}", LogHelper.convertObjectToJson(produtoDTO)));
        if (Objects.nonNull(seguroCalculado)){
            logger.info(MessageFormat.format("Seguro calculado com sucesso: {0}", LogHelper.convertObjectToJson(seguroCalculado)));
            return ResponseEntity.ok(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado para atualizar");
    }
}
