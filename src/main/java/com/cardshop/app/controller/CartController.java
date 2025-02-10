package com.cardshop.app.controller;

import com.cardshop.app.model.CartItem;
import com.cardshop.app.model.User;
import com.cardshop.app.service.CartService;
import com.cardshop.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Корзина", description = "Операции с корзиной")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить корзину пользователя")
    @ApiResponse(responseCode = "200", description = "Корзина пользователя успешно получена")
    public ResponseEntity<List<CartItem>> getUserCart(
        @Parameter(description = "ID пользователя", required = true) 
        @PathVariable Long userId
    ) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return ResponseEntity.ok(cartService.getUserCart(user));
    }

    @PostMapping("/{userId}/add")
    @Operation(summary = "Добавить товар в корзину")
    @ApiResponse(responseCode = "201", description = "Товар успешно добавлен в корзину")
    public ResponseEntity<CartItem> addToCart(
        @Parameter(description = "ID пользователя", required = true) 
        @PathVariable Long userId,
        @Parameter(description = "ID товара", required = true) 
        @RequestParam Long productId,
        @Parameter(description = "Количество товара", required = true) 
        @RequestParam Integer quantity
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "Удалить товар из корзины")
    @ApiResponse(responseCode = "204", description = "Товар успешно удален из корзины")
    public ResponseEntity<Void> removeFromCart(
        @Parameter(description = "ID элемента корзины", required = true) 
        @PathVariable Long cartItemId
    ) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
