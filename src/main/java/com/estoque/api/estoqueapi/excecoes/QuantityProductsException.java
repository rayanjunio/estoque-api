package com.estoque.api.estoqueapi.excecoes;

public class QuantityProductsException extends RuntimeException {
  public QuantityProductsException() {
    super("Digite uma quantidade de produtos válida!");
  }

  public QuantityProductsException(String message) {
    super(message);
  }
}
