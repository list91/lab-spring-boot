package com.cardshop.app.service;

import com.cardshop.app.model.CartItem;
import com.cardshop.app.model.Product;
import com.cardshop.app.model.User;
import com.cardshop.app.repository.CartItemRepository;
import com.cardshop.app.repository.ProductRepository;
import com.cardshop.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Card");
        testProduct.setPrice(BigDecimal.valueOf(10.99));
        testProduct.setStockQuantity(100);

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUser(testUser);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
        testCartItem.updateTotalPrice();
    }

    @Test
    void addToCart_ShouldCreateCartItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartItem cartItem = cartService.addToCart(1L, 1L, 2);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());
        assertEquals(98, testProduct.getStockQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
        verify(productRepository).save(testProduct);
    }

    @Test
    void addToCart_InsufficientStock_ShouldThrowException() {
        testProduct.setStockQuantity(1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(RuntimeException.class, () -> cartService.addToCart(1L, 1L, 2));
    }

    @Test
    void removeFromCart_ShouldDeleteCartItemAndRestoreStock() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        cartService.removeFromCart(1L);

        verify(cartItemRepository).delete(testCartItem);
        assertEquals(102, testProduct.getStockQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void getUserCart_ShouldReturnUserCartItems() {
        when(cartItemRepository.findByUser(testUser)).thenReturn(List.of(testCartItem));

        List<CartItem> cartItems = cartService.getUserCart(testUser);

        assertFalse(cartItems.isEmpty());
        assertEquals(1, cartItems.size());
        assertEquals(testCartItem, cartItems.get(0));
    }
}
