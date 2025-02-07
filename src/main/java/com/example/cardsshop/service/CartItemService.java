package com.example.cardsshop.service;

import com.example.cardsshop.exception.ResourceNotFoundException;
import com.example.cardsshop.model.CartItem;
import com.example.cardsshop.model.Product;
import com.example.cardsshop.model.User;
import com.example.cardsshop.repository.CartItemRepository;
import com.example.cardsshop.repository.ProductRepository;
import com.example.cardsshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItem addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Товар не найден"));
        
        Optional<CartItem> existingCartItem = cartItemRepository
                .findByUser_IdAndProduct_Id(userId, productId);
        
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
        }
        
        log.info("Добавление товара в корзину: userId={}, productId={}, quantity={}", 
                userId, productId, quantity);
        
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        CartItem cartItem = cartItemRepository
                .findByUser_IdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Товар не найден в корзине"));
        
        cartItemRepository.delete(cartItem);
        
        log.info("Удаление товара из корзины: userId={}, productId={}", userId, productId);
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
        
        log.info("Получение товаров корзины: userId={}, itemCount={}", userId, cartItems.size());
        
        return cartItems;
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteAllByUserId(userId);
        
        log.info("Очистка корзины: userId={}", userId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPrice(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
        
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        log.info("Расчет общей стоимости корзины: userId={}, totalPrice={}", userId, totalPrice);
        
        return totalPrice;
    }

    @Transactional
    public CartItem updateQuantity(Long userId, Long productId, int quantity) {
        CartItem cartItem = cartItemRepository
                .findByUser_IdAndProduct_Id(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Товар не найден в корзине"));
        
        cartItem.setQuantity(quantity);
        
        log.info("Обновление количества товара в корзине: userId={}, productId={}, quantity={}", 
                userId, productId, quantity);
        
        return cartItemRepository.save(cartItem);
    }
}
