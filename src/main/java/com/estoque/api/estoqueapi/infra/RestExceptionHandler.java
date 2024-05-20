package com.estoque.api.estoqueapi.infra;

import com.estoque.api.estoqueapi.excecoes.CodeNotFoundException;
import com.estoque.api.estoqueapi.excecoes.QuantityProductsException;
import com.estoque.api.estoqueapi.excecoes.ValueProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler  {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestErrorMessage> methodArgumentNotValidHandler(
          MethodArgumentNotValidException exception) {
    RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST,
            "Digite uma informação válida!");
    return ResponseEntity.status(errorMessage.getStatus()).body(errorMessage);
  }

  @ExceptionHandler(CodeNotFoundException.class)
  private ResponseEntity<RestErrorMessage> codeNotFoundHandler(CodeNotFoundException exception) {
    RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @ExceptionHandler(ValueProductException.class)
  private ResponseEntity<RestErrorMessage> valueProductHandler(ValueProductException exception) {
    RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
  }

  @ExceptionHandler(QuantityProductsException.class)
  private ResponseEntity<RestErrorMessage> quantityProductsHandler(QuantityProductsException exception) {
    RestErrorMessage errorMessage = new RestErrorMessage(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
  }
}
