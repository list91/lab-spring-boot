package com.example.cardsshop.controller;

import com.example.cardsshop.model.Category;
import com.example.cardsshop.model.Product;
import com.example.cardsshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Тестовая категория", "Описание категории");
        testProduct = new Product("Тестовый товар", "Описание товара", 
                                  BigDecimal.valueOf(100.50), testCategory);
        testProduct.setId(1L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(1))
               .andExpect(jsonPath("$[0].name").value("Тестовый товар"));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Тестовый товар"));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("Тестовый товар"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Тестовый товар"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
               .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        when(productService.getProductsByCategory("Тестовая категория"))
            .thenReturn(List.of(testProduct));

        mockMvc.perform(get("/api/products/category/Тестовая категория"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(1))
               .andExpect(jsonPath("$[0].name").value("Тестовый товар"));
    }
}
