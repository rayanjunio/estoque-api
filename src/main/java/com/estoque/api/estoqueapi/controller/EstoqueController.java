package com.estoque.api.estoqueapi.controller;

import com.estoque.api.estoqueapi.model.Produto;
import com.estoque.api.estoqueapi.model.ReporEstoqueRequest;
import com.estoque.api.estoqueapi.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

  @Autowired
  private EstoqueService service;

  @PostMapping("/cadastrar")
  public ResponseEntity<?> cadastrar(@Valid @RequestBody Produto obj) {
    return new ResponseEntity<>(service.cadastrar(obj), HttpStatus.CREATED);
  }

  @GetMapping("/selecionar-tudo")
  public ResponseEntity<?> listarProdutos() {
    return new ResponseEntity<>(service.selecionarProdutos(), HttpStatus.OK);
  }

  @PutMapping("/editar-produto")
  public ResponseEntity<?> editar(@Valid @RequestBody Produto obj) {
    return service.editar(obj);
  }

  @DeleteMapping("/excluir-produto/{codigo}")
  public ResponseEntity<?> excluir(@PathVariable int codigo) {
    return new ResponseEntity<>(service.excluir(codigo), HttpStatus.OK);
  }

  @PatchMapping(value = "/vender", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> vender(@Valid @RequestBody List<Integer> idProdutos) {
    return new ResponseEntity<>(service.vender(idProdutos), HttpStatus.OK);
  }

  @GetMapping("/produto-valor/{codigo}")
  public ResponseEntity<?> exibirValor(@Valid @PathVariable int codigo) {
    return new ResponseEntity<>(service.exibirValor(codigo), HttpStatus.OK);
  }

  @PatchMapping(value = "/produto/adicionar", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> reporEstoque(@Valid @RequestBody ReporEstoqueRequest reposicao) {
    return new ResponseEntity<>(service.reporEstoque(reposicao), HttpStatus.OK);
  }
}
