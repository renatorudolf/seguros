package br.com.itau.seguros.adapters.inbound;

import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
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
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

@RestController
@RequestMapping("/seguro")
public class CalculaTarifaController {

    Logger logger = LoggerFactory.getLogger(CalculaTarifaController.class);

    @Autowired
    private CalculaTarifaServicePort calculaTarifaService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> calcularImposto(@RequestBody @Valid ProdutoRequest payload) {
        var correlationId = UUID.randomUUID().toString();
        LogHelper.printLog(Level.INFO, "Payload de requisicao recebido: ",payload , correlationId, CalculaTarifaController.class);
        LogHelper.printLog(Level.INFO, "Convertendo requisicao em objeto de dominio", CalculaTarifaController.class);
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        LogHelper.printLog(Level.INFO, "Convertido com sucesso requisicao em objeto de dominio: ", produtoDTO, correlationId, CalculaTarifaController.class);
        var seguroCalculado = calculaTarifaService.cadastrarSeguro(produtoDTO, correlationId);
        if (Objects.nonNull(seguroCalculado)) {
            LogHelper.printLog(Level.INFO, "Seguro cadastrado e calculado com sucesso: ", seguroCalculado, correlationId, CalculaTarifaController.class);
            return new ResponseEntity<>(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado), HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Recurso existente na base");
        }

    @PatchMapping("/{categoria}")
    public ResponseEntity<ProdutoResponse> atualizarCalculoImposto(@RequestBody @Valid ProdutoRequest payload, @PathVariable("categoria") String categoria){
        var correlationId = UUID.randomUUID().toString();
        LogHelper.printLog(Level.INFO, "Payload de requisicao recebido: ",payload , correlationId, CalculaTarifaController.class);
        LogHelper.printLog(Level.INFO, "Convertendo requisicao em objeto de dominio", CalculaTarifaController.class);
        var produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        LogHelper.printLog(Level.INFO, "Convertido com sucesso requisicao em objeto de dominio: ", produtoDTO, correlationId, CalculaTarifaController.class);
        var seguroCalculado = calculaTarifaService.atualizarCalculoSeguro(produtoDTO, categoria, correlationId);
        if (Objects.nonNull(seguroCalculado)){
            LogHelper.printLog(Level.INFO, "Seguro atualizado e calculado com sucesso: ", seguroCalculado, correlationId, CalculaTarifaController.class);
            return ResponseEntity.ok(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado para atualizar");
    }
}
