package com.estoque.api.estoqueapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
public class Produto {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int codigo;

  @Column(length = 50)
  @NotNull
  @NotEmpty
  @NotBlank
  private String nome;

  @Column
  private double valor;

  @Column
  private int quantidade;
}
