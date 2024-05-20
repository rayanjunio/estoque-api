package com.estoque.api.estoqueapi.repository;

import com.estoque.api.estoqueapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Produto, Integer> {
  List<Produto> findAll();
  Produto findByCodigo(int codigo);
}
