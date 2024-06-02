package com.estoque.api.estoqueapi.service;

import com.estoque.api.estoqueapi.excecoes.CodeNotFoundException;
import com.estoque.api.estoqueapi.excecoes.InvalidInformationException;
import com.estoque.api.estoqueapi.model.Produto;
import com.estoque.api.estoqueapi.model.ReporEstoqueRequest;
import com.estoque.api.estoqueapi.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {
  @Autowired
  private EstoqueRepository estoqueRepository;

  public Produto cadastrar(Produto produto) {
    if (produto.getQuantidade() <= 0 || produto.getValor() <= 0) {
      throw new InvalidInformationException();
    }
    Produto productCreated = estoqueRepository.save(produto);
    return productCreated;
  }

  public List<Produto> selecionarProdutos() {
    List<Produto> produtos = estoqueRepository.findAll();
    return produtos;
  }

  public Produto editar(Produto obj) {
    Produto produto = estoqueRepository.findByCodigo(obj.getCodigo());
    if (produto == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + obj.getCodigo());
    }

    if (obj.getQuantidade() <= 0 || obj.getValor() <= 0) {
      throw new InvalidInformationException();
    }
    estoqueRepository.save(produto);
    return produto;
  }

  public String excluir(int idProduto) {
    Produto produto = estoqueRepository.findByCodigo(idProduto);
    if (produto == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + idProduto);
    }
    estoqueRepository.delete(produto);
    return produto.getNome() + " deletado com sucesso!";
  }

  public String vender(List<Integer> idProdutos) {
    for (Integer idProduto : idProdutos) {
      if (estoqueRepository.findByCodigo(idProduto) == null) {
        throw new CodeNotFoundException("Produto não encontrado para o código " + idProduto);
      }
    }
    Produto produto;
    double valueProducts = 0;

    for (Integer idProduto : idProdutos) {
      produto = estoqueRepository.findByCodigo(idProduto);
      valueProducts += produto.getValor();
      produto.setQuantidade(produto.getQuantidade() - 1);
      estoqueRepository.save(produto);
    }
    return "Valor total: " + valueProducts;
  }

  public double exibirValor(int idProduto) {
    Produto produto = estoqueRepository.findByCodigo(idProduto);
    if (produto == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + idProduto);
    }
    return produto.getValor();
  }

  public Produto reporEstoque(ReporEstoqueRequest reposicao) {
    if (reposicao.getQuantidade() <= 0) {
      throw new InvalidInformationException("A quantidade de itens não é aceita");
    }
    Produto produto = estoqueRepository.findByCodigo(reposicao.getIdProduto());
    if (produto == null) {
      throw new CodeNotFoundException("Produto não encontrado para o código " + reposicao.getIdProduto());
    }
    produto.setQuantidade(produto.getQuantidade() + reposicao.getQuantidade());
    estoqueRepository.save(produto);
    return produto;
  }
}
