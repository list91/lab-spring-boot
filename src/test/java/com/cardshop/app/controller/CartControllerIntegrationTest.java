package com.cardshop.app.controller;

import com.cardshop.app.model.CartItem;
import com.cardshop.app.model.Product;
import com.cardshop.app.model.User;
import com.cardshop.app.repository.CartItemRepository;
import com.cardshop.app.repository.ProductRepository;
import com.cardshop.app.repository.UserRepository;
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
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        cartItemRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        testProduct = new Product();
        testProduct.setName("Test Card");
        testProduct.setPrice(BigDecimal.valueOf(10.99));
        testProduct.setStockQuantity(100);
        testProduct = productRepository.save(testProduct);

        testCartItem = new CartItem();
        testCartItem.setUser(testUser);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
        testCartItem.updateTotalPrice();
        testCartItem = cartItemRepository.save(testCartItem);
    }

    @Test
    void getUserCart_ShouldReturnUserCartItems() throws Exception {
        mockMvc.perform(get("/api/cart/{userId}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    void addToCart_ShouldCreateCartItem() throws Exception {
        mockMvc.perform(post("/api/cart/{userId}/add", testUser.getId())
                .param("productId", testProduct.getId().toString())
                .param("quantity", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void removeFromCart_ShouldDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/api/cart/{cartItemId}", testCartItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cart/{userId}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
