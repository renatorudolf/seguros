package br.com.itau.seguros.adapters.inbound;

import br.com.itau.seguros.adapters.inbound.mapper.ProdutoMapper;
import br.com.itau.seguros.core.domain.ProdutoDTO;
import br.com.itau.seguros.core.domain.ProdutoRequest;
import br.com.itau.seguros.core.domain.ProdutoResponse;
import br.com.itau.seguros.ports.in.CalculaTarifaServicePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/seguro")
public class CalculaTarifaController {

    Logger log = Logger.getLogger(CalculaTarifaController.class.getName());

    @Autowired
    private CalculaTarifaServicePort calculaTarifaService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> calcularImposto(@RequestBody @Valid ProdutoRequest payload){
        ProdutoDTO produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        ProdutoDTO seguroCalculado = calculaTarifaService.calcularSeguro(produtoDTO);
        if (Objects.nonNull(seguroCalculado)) {
            return new ResponseEntity<>(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado), HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Recurso existente na base");
        }

    @PatchMapping("/{categoria}")
    public ResponseEntity<ProdutoResponse> atualizarCalculoImposto(@RequestBody @Valid ProdutoRequest payload, @PathVariable("categoria") String categoria){
        ProdutoDTO produtoDTO = ProdutoMapper.INSTANCE.produtoRequestToProdutoDto(payload);
        ProdutoDTO seguroCalculado = calculaTarifaService.atualizarCalculoSeguro(produtoDTO, categoria);
        if (Objects.nonNull(seguroCalculado)){
            return ResponseEntity.ok(ProdutoMapper.INSTANCE.produtoDtoToProdutoResponse(seguroCalculado));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado para atualizar");
    }
}
