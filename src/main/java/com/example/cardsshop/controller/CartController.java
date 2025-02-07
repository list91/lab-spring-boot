package com.example.cardsshop.controller;

import com.example.cardsshop.model.CartItem;
import com.example.cardsshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<CartItem> addToCart(
            @PathVariable Long userId, 
            @PathVariable Long productId, 
            @RequestParam(defaultValue = "1") Integer quantity) {
        CartItem cartItem = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long userId, 
            @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
}
