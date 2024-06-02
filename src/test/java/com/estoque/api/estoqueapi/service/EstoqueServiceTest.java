package com.estoque.api.estoqueapi.service;

import com.estoque.api.estoqueapi.excecoes.CodeNotFoundException;
import com.estoque.api.estoqueapi.excecoes.InvalidInformationException;
import com.estoque.api.estoqueapi.model.Produto;
import com.estoque.api.estoqueapi.model.ReporEstoqueRequest;
import com.estoque.api.estoqueapi.repository.EstoqueRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

  @InjectMocks
  EstoqueService estoqueService;

  @Mock
  EstoqueRepository estoqueRepository;

  @Test
  @DisplayName("Should return product after it is created")
  void cadastrar_ShouldReturnProductCreated_WhenDataAreValid() {
    Produto produto = new Produto("arroz", 12.79, 25);

    when(estoqueRepository.save(produto)).thenReturn(produto);

    assertEquals(produto, estoqueService.cadastrar(produto));

    verify(estoqueRepository,times(1)).save(produto);
  }

  @Test
  @DisplayName("Should throw an exception when some product attribute is invalid")
  void cadastrar_ShouldThrowException_WhenSomeProductAttributeIsInvalid() {
    Produto produto = new Produto("arroz", 5, 0);

    InvalidInformationException exception = assertThrows(InvalidInformationException.class,
            () -> estoqueService.cadastrar(produto));
    assertEquals("Check the data from product", exception.getMessage());

    verify(estoqueRepository,times(0)).save(produto);
  }

  @Test
  @DisplayName("Should return a product list when there are products registered")
  void selecionarProdutos_ShouldReturnAProductsList_WhenThereAreProductsRegistered() {
    List<Produto> produtos = List.of(new Produto("arroz", 5, 15));

    when(estoqueService.selecionarProdutos()).thenReturn(produtos);

    assertEquals(produtos, estoqueService.selecionarProdutos());
  }

  @Test
  @DisplayName("Should return an empty list when there are no products registered")
  void selecionarProdutos_ShouldReturnAnEmptyList_WhenThereAreNoProductsRegistered() {
    List<Produto> produtos = new ArrayList<>();

    when(estoqueService.selecionarProdutos()).thenReturn(produtos);

    assertEquals(produtos.size(), estoqueService.selecionarProdutos().size());
  }

  @Test
  @DisplayName("Should return the edited product when product data is valid")
  void editar_ShouldReturnTheEditedProduct_WhenProductDataIsValid() {
    Produto produto = new Produto("arroz", 5, 15);
    produto.setCodigo(2);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(produto);

    produto.setNome("feijão");
    produto.setQuantidade(20);
    produto.setValor(8.99);

    assertEquals(produto, estoqueService.editar(produto));
  }

  @Test
  @DisplayName("Should return an exception when the product code does not exist")
  void editar_ShouldThrowException_WhenTheProductCodeDoesNotExist() {
    Produto produto = new Produto("arroz", 5, 15);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(null);

    CodeNotFoundException exception = assertThrows(CodeNotFoundException.class,
            () -> estoqueService.editar(produto));
    assertEquals("Produto não encontrado para o código " + produto.getCodigo(), exception.getMessage());

    verify(estoqueRepository, times(0)).save(produto);
  }

  @Test
  @DisplayName("Should throw an exception when some attribute of the product is invalid")
  void editar_ShouldThrowException_WhenSomeAttributeFromProductIsInvalid() {
    Produto produto = new Produto("arroz", 0, 15);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(produto);

    InvalidInformationException exception = assertThrows(InvalidInformationException.class,
            () -> estoqueService.editar(produto));
    assertEquals("Check the data from product", exception.getMessage());

    verify(estoqueRepository, times(0)).save(produto);
  }

  @Test
  @DisplayName("Should return a message saying that the product was successfully deleted")
  void excluir_ShouldReturnMessage_WhenTheProductWasDeleted() {
    Produto produto = new Produto("arroz", 27.99, 15);
    produto.setCodigo(2);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(produto);

    assertEquals(produto.getNome() + " deletado com sucesso!", estoqueService.excluir(produto.getCodigo()));
  }

  @Test
  @DisplayName("Should throw an exception when the product code is invalid")
  void excluir_ThrowException_WhenTheProductCodeIsInvalid() {
    Produto produto = new Produto("arroz", 27.99, 15);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(null);

    CodeNotFoundException exception = assertThrows(CodeNotFoundException.class,
            () -> estoqueService.excluir(produto.getCodigo()));
    assertEquals("Produto não encontrado para o código " + produto.getCodigo(), exception.getMessage());

    verify(estoqueRepository, times(0)).delete(produto);
  }

  @Test
  @DisplayName("Should return value of the shopping when the product codes are valid")
  void vender_ReturnValueOfShopping_WhenProductCodesAreValid() {
    Produto firstProduct = new Produto("arroz", 27.99, 15);
    Produto secondProduct = new Produto("feijão", 15.89, 20);

    firstProduct.setCodigo(1);
    secondProduct.setCodigo(2);

    List<Integer> ids = List.of(firstProduct.getCodigo(), secondProduct.getCodigo());

    when(estoqueRepository.findByCodigo(firstProduct.getCodigo())).thenReturn(firstProduct);
    when(estoqueRepository.findByCodigo(secondProduct.getCodigo())).thenReturn(secondProduct);

    double sum = firstProduct.getValor() + secondProduct.getValor();

    assertEquals("Valor total: " + sum, estoqueService.vender(ids));
  }

  @Test
  @DisplayName("Should throw an exception when one or more product codes are invalids")
  void vender_ThrowException_WhenOneOrMoreProductCodesAreInvalids() {
    Produto firstProduct = new Produto("arroz", 27.99, 15);
    Produto secondProduct = new Produto("feijão", 15.89, 20);

    firstProduct.setCodigo(1);

    List<Integer> ids = List.of(firstProduct.getCodigo(), secondProduct.getCodigo());

    when(estoqueRepository.findByCodigo(firstProduct.getCodigo())).thenReturn(firstProduct);
    when(estoqueRepository.findByCodigo(secondProduct.getCodigo())).thenReturn(null);

    CodeNotFoundException exception = assertThrows(CodeNotFoundException.class,
            () -> estoqueService.vender(ids));
    assertEquals("Produto não encontrado para o código " + secondProduct.getCodigo(), exception.getMessage());

    verify(estoqueRepository, times(0)).save(any());
  }

  @Test
  @DisplayName("Should return the value from product when the code is valid")
  void exibirValor_ReturnValueFromProduct_WhenTheProductCodeIsValid() {
    Produto produto = new Produto("arroz", 27.99, 15);
    produto.setCodigo(2);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(produto);

    assertEquals(produto.getValor(), estoqueService.exibirValor(produto.getCodigo()));
  }

  @Test
  @DisplayName("Should throw an exception when the product code is invalid")
  void exibirValor_ThrowException_WhenTheProductCodeIsInvalid() {
    Produto produto = new Produto("arroz", 27.99, 15);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(null);

    CodeNotFoundException exception = assertThrows(CodeNotFoundException.class,
            () -> estoqueService.exibirValor(produto.getCodigo()));
    assertEquals("Produto não encontrado para o código " + produto.getCodigo(), exception.getMessage());
  }

  @Test
  @DisplayName("Should return the updated product when code and quantity are valid")
  void reporEstoque_ReturnProductAfterUpdate_WhenCodeAndQuantityAreValid() {
    Produto produto = new Produto("arroz", 27.99, 10);
    produto.setCodigo(1);

    ReporEstoqueRequest estoqueRequest = new ReporEstoqueRequest(produto.getCodigo(), 10);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(produto);

    Produto produtoRetornado = estoqueService.reporEstoque(estoqueRequest);

    assertEquals(20, produtoRetornado.getQuantidade());
  }

  @Test
  @DisplayName("Should throw an exception when product code is invalid")
  void reporEstoque_ThrowException_WhenProductCodeIsInvalid() {
    Produto produto = new Produto("arroz", 27.99, 10);

    ReporEstoqueRequest estoqueRequest = new ReporEstoqueRequest(produto.getCodigo(), 10);

    when(estoqueRepository.findByCodigo(produto.getCodigo())).thenReturn(null);

    CodeNotFoundException exception = assertThrows(CodeNotFoundException.class,
            () -> estoqueService.reporEstoque(estoqueRequest));
    assertEquals("Produto não encontrado para o código " + estoqueRequest.getIdProduto(), exception.getMessage());
  }

  @Test
  @DisplayName("Should throw an exception when product quantity is invalid")
  void reporEstoque_ThrowException_WhenProductQuantityIsInvalid() {
    Produto produto = new Produto("arroz", 27.99, 10);
    produto.setCodigo(1);

    ReporEstoqueRequest estoqueRequest = new ReporEstoqueRequest(produto.getCodigo(), 0);

    InvalidInformationException exception = assertThrows(InvalidInformationException.class,
            () -> estoqueService.reporEstoque(estoqueRequest));
    assertEquals("A quantidade de itens não é aceita", exception.getMessage());
  }
}