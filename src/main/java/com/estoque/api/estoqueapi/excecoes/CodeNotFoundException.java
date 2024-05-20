package com.estoque.api.estoqueapi.excecoes;

public class CodeNotFoundException extends RuntimeException {
  public CodeNotFoundException() {
    super("O Código não existe!");
  }

  public CodeNotFoundException(String message) {
    super(message);
  }
}
