package com.example.cardsshop.controller;

import com.example.cardsshop.model.CartItem;
import com.example.cardsshop.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Управление корзиной", description = "Операции с товарами в корзине")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/add")
    @Operation(summary = "Добавление товара в корзину")
    public ResponseEntity<CartItem> addToCart(
            @RequestParam @Min(1) Long userId, 
            @RequestParam @Min(1) Long productId, 
            @RequestParam @Min(1) int quantity) {
        CartItem cartItem = cartItemService.addToCart(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Удаление товара из корзины")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam @Min(1) Long userId, 
            @RequestParam @Min(1) Long productId) {
        cartItemService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Получение товаров в корзине")
    public ResponseEntity<List<CartItem>> getCartItems(
            @PathVariable @Min(1) Long userId) {
        List<CartItem> cartItems = cartItemService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/clear/{userId}")
    @Operation(summary = "Очистка корзины")
    public ResponseEntity<Void> clearCart(
            @PathVariable @Min(1) Long userId) {
        cartItemService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total/{userId}")
    @Operation(summary = "Расчет общей стоимости корзины")
    public ResponseEntity<BigDecimal> calculateTotalPrice(
            @PathVariable @Min(1) Long userId) {
        BigDecimal totalPrice = cartItemService.calculateTotalPrice(userId);
        return ResponseEntity.ok(totalPrice);
    }

    @PutMapping("/update")
    @Operation(summary = "Обновление количества товара в корзине")
    public ResponseEntity<CartItem> updateQuantity(
            @RequestParam @Min(1) Long userId, 
            @RequestParam @Min(1) Long productId, 
            @RequestParam @Min(1) int quantity) {
        CartItem cartItem = cartItemService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }
}
