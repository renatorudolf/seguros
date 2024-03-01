package br.com.itau.seguros.adapters.outbound.repository;

import br.com.itau.seguros.adapters.outbound.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    ProdutoEntity findByCategoriaNome(String categoriaNome);


}
