package com.estoque.api.estoqueapi.service;

import com.estoque.api.estoqueapi.excecoes.CodeNotFoundException;
import com.estoque.api.estoqueapi.excecoes.QuantityProductsException;
import com.estoque.api.estoqueapi.excecoes.ValueProductException;
import com.estoque.api.estoqueapi.model.Produto;
import com.estoque.api.estoqueapi.model.ReporEstoqueRequest;
import com.estoque.api.estoqueapi.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
  @Autowired
  private EstoqueRepository estoqueRepository;

  public ResponseEntity<?> cadastrar(Produto obj) {
    validacaoDados(obj);
    return new ResponseEntity<>(estoqueRepository.save(obj), HttpStatus.CREATED);
  }

  public ResponseEntity<?> selecionarProdutos() {
    if (estoqueRepository.count() == 0) {
      return new ResponseEntity<>("O estoque não possui produtos cadastrados", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(estoqueRepository.findAll(), HttpStatus.OK);
    }
  }

  public Produto selecionarPorCodigo(int idProduto) {
    if (estoqueRepository.findByCodigo(idProduto) == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + idProduto);
    } else {
      return estoqueRepository.findByCodigo(idProduto);
    }
  }

  public ResponseEntity<?> editar(Produto obj) {
    if (estoqueRepository.findByCodigo(obj.getCodigo()) == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + obj.getCodigo());
    } else {
      validacaoDados(obj);
      return new ResponseEntity<>(estoqueRepository.save(obj), HttpStatus.OK);
    }
  }

  public ResponseEntity<?> excluir(int idProduto) {
    Produto obj = selecionarPorCodigo(idProduto);
    estoqueRepository.delete(obj);
    return new ResponseEntity<>("Produto deletado com sucesso!", HttpStatus.OK);
  }

  public ResponseEntity<?> vender(List<Integer> idProdutos) {
    for (Integer idProduto : idProdutos) {
      if (estoqueRepository.findByCodigo(idProduto) == null) {
        throw new CodeNotFoundException("Produto não encontrado para o código: " + idProduto);
      } else {
        Produto obj = estoqueRepository.findByCodigo(idProduto);
        obj.setQuantidade(obj.getQuantidade() - 1);
        estoqueRepository.save(obj);
      }
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<?> exibirValor(int idProduto) {
    Produto obj = selecionarPorCodigo(idProduto);
    return new ResponseEntity<>(obj.getValor(), HttpStatus.OK);
  }

  public ResponseEntity<?> reporEstoque(ReporEstoqueRequest reposicao) {
    if (reposicao.getQuantidade() <= 0) {
      throw new QuantityProductsException();
    } else {
      Produto obj = selecionarPorCodigo(reposicao.getIdProduto());
      obj.setQuantidade(obj.getQuantidade() + reposicao.getQuantidade());
      return new ResponseEntity<>(estoqueRepository.save(obj), HttpStatus.OK);
    }
  }

  private void validacaoDados(Produto obj) {
    if (obj.getQuantidade() <= 0) {
      throw new QuantityProductsException();
    } else if (obj.getValor() <= 0) {
      throw new ValueProductException();
    }
  }
}
