package com.estoque.api.estoqueapi.controller;

import com.estoque.api.estoqueapi.model.Produto;
import com.estoque.api.estoqueapi.model.ReporEstoqueRequest;
import com.estoque.api.estoqueapi.service.EstoqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class EstoqueControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  EstoqueService estoqueService;

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  @DisplayName("Should return the product after it has been created")
  void cadastrar_ShouldReturnANewProduct() throws Exception {
    Produto product = new Produto("arroz", 12.79, 25);
    product.setCodigo(4);

    when(estoqueService.cadastrar(any(Produto.class))).thenReturn(product);

    mockMvc.perform(
            post("/estoque/cadastrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(product)))
            .andExpect(status().isCreated()
    )
            .andExpect(jsonPath("$.codigo", is(4)))
            .andExpect(jsonPath("$.nome", is("arroz")))
            .andExpect(jsonPath("$.valor", is(12.79)))
            .andExpect(jsonPath("$.quantidade", is(25)));

    verify(estoqueService, times(1)).cadastrar(any(Produto.class));
  }

  @Test
  @DisplayName("Should return the product value")
  void exibirValor_ShouldReturnProductValue() throws Exception {
    Produto product = new Produto("arroz", 12.79, 25);
    product.setCodigo(4);

    when(estoqueService.exibirValor(product.getCodigo())).thenReturn(product.getValor());

    mockMvc.perform(
            get("/estoque/produto-valor/" + product.getCodigo())
            .accept(MediaType.APPLICATION_JSON)
    )
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(product.getValor())));
  }

  @Test
  @DisplayName("Should return the product after it has been updated")
  void editarProduto_ShouldReturnUpdatedProduct() throws Exception {
    Produto product = new Produto("arroz", 15.79, 45);
    product.setCodigo(2);

    when(estoqueService.editar(any(Produto.class))).thenReturn(product);

    mockMvc.perform(
            put("/estoque/editar-produto")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(product)))
            .andExpect(status().isOk()
    )
            .andExpect(jsonPath("$.codigo", is(2)))
            .andExpect(jsonPath("$.nome", is("arroz")))
            .andExpect(jsonPath("$.valor", is(15.79)))
            .andExpect(jsonPath("$.quantidade", is(45)));

    verify(estoqueService, times(1)).editar(any(Produto.class));
  }

  @Test
  @DisplayName("Should return a success message confirming that product was deleted")
  void excluir_ShouldReturnASuccessMessage() throws Exception {
    Produto product = new Produto("arroz", 15.79, 45);
    product.setCodigo(2);

    String expectedMessage = product.getNome() + " deletado com sucesso!";

    when(estoqueService.excluir(anyInt())).thenReturn(expectedMessage);

    mockMvc.perform(
            delete("/estoque/excluir-produto/" + product.getCodigo())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedMessage));

    verify(estoqueService, times(1)).excluir(anyInt());
  }

  @Test
  @DisplayName("Should return the total sum of products value")
  void vender_ShouldReturnTheTotalSumOfProductsValue() throws Exception {
    Produto firstProduct = new Produto("arroz", 15.79, 45);
    Produto secondProduct = new Produto("feijão", 14.89, 20);

    firstProduct.setCodigo(2);
    secondProduct.setCodigo(3);

    List<Integer> ids = List.of(firstProduct.getCodigo(), secondProduct.getCodigo());

    String expectedMessage = "Valor total: " + firstProduct.getValor() + secondProduct.getValor();

    when(estoqueService.vender(ids)).thenReturn(expectedMessage);

    mockMvc.perform(
            patch("/estoque/vender")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(ids)))
            .andExpect(status().isOk()
    )
            .andExpect(content().string(expectedMessage));

    verify(estoqueService, times(1)).vender(anyList());
  }
  @Test
  @DisplayName("Should return the product after it has been updated")
  void reporEstoque_ShouldReturnUpdatedProduct() throws Exception {
    Produto product = new Produto("arroz", 15.79, 45);
    product.setCodigo(3);

    ReporEstoqueRequest estoqueRequest = new ReporEstoqueRequest(product.getCodigo(), 20);

    product.setQuantidade(product.getQuantidade() + estoqueRequest.getQuantidade());

    when(estoqueService.reporEstoque(any())).thenReturn(product);

    mockMvc.perform(
            patch("/estoque/produto/adicionar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(estoqueRequest)))
            .andExpect(status().isOk()
    )
            .andExpect(jsonPath("$.nome", is(product.getNome())))
            .andExpect(jsonPath("$.valor", is(product.getValor())))
            .andExpect(jsonPath("$.quantidade", is(product.getQuantidade())));

    verify(estoqueService, times(1)).reporEstoque(any(ReporEstoqueRequest.class));
  }
}