package com.estoque.api.estoqueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReporEstoqueRequest {
  private int idProduto;
  private int quantidade;
}
