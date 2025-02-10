package com.cardshop.app.controller;

import com.cardshop.app.model.Product;
import com.cardshop.app.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        testProduct = new Product();
        testProduct.setName("Test Card");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(BigDecimal.valueOf(10.99));
        testProduct.setCategory("Magic");
        testProduct.setStockQuantity(100);

        testProduct = productRepository.save(testProduct);
    }

    @Test
    void getAllProducts_ShouldReturnProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Card"));
    }

    @Test
    void getProductById_ExistingProduct_ShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Card"));
    }

    @Test
    void createProduct_ShouldCreateAndReturnProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Card");
        newProduct.setPrice(BigDecimal.valueOf(15.99));
        newProduct.setCategory("Pokemon");
        newProduct.setStockQuantity(50);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Card"))
                .andExpect(jsonPath("$.category").value("Pokemon"));
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct() throws Exception {
        testProduct.setName("Updated Card");
        testProduct.setPrice(BigDecimal.valueOf(20.99));

        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Card"))
                .andExpect(jsonPath("$.price").value(20.99));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
