package com.estoque.api.estoqueapi.excecoes;

public class InvalidInformationException extends RuntimeException{
  public InvalidInformationException() {
    super("Check the data from product");
  }

  public InvalidInformationException(String message) {
    super(message);
  }
}
