package com.example.cardsshop.service;

import com.example.cardsshop.model.CartItem;
import com.example.cardsshop.model.Product;
import com.example.cardsshop.model.User;
import com.example.cardsshop.repository.CartItemRepository;
import com.example.cardsshop.repository.ProductRepository;
import com.example.cardsshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct_Id(user, productId);
        
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem(product, user, quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        CartItem cartItem = cartItemRepository.findByUserAndProduct_Id(user, productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден в корзине"));
        
        cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        return cartItemRepository.findByUser(user);
    }
}
