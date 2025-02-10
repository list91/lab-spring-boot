package com.cardshop.app.service;

import com.cardshop.app.model.CartItem;
import com.cardshop.app.model.Product;
import com.cardshop.app.model.User;
import com.cardshop.app.repository.CartItemRepository;
import com.cardshop.app.repository.ProductRepository;
import com.cardshop.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CartItem> getUserCart(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Недостаточно товара на складе");
        }

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.updateTotalPrice();

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Товар в корзине не найден"));

        Product product = cartItem.getProduct();
        product.setStockQuantity(product.getStockQuantity() + cartItem.getQuantity());
        productRepository.save(product);

        cartItemRepository.delete(cartItem);
    }
}
