package com.estoque.api.estoqueapi.excecoes;

public class ValueProductException extends RuntimeException {
  public ValueProductException() {
    super("Digite um valor válido para o produto!");
  }

  public ValueProductException(String message) {
    super(message);
  }
}
