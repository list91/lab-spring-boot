package com.cardshop.app.controller;

import com.cardshop.app.model.Product;
import com.cardshop.app.service.ProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Товары", description = "Операции с товарами")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Получить список всех товаров")
    @ApiResponse(responseCode = "200", description = "Список товаров успешно получен")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    @ApiResponse(responseCode = "200", description = "Товар найден")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    public ResponseEntity<Product> getProductById(
        @Parameter(description = "ID товара", required = true) 
        @PathVariable Long id
    ) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Получить список товаров по категории")
    @ApiResponse(responseCode = "200", description = "Список товаров успешно получен")
    public ResponseEntity<List<Product>> getProductsByCategory(
        @Parameter(description = "Категория товара", required = true) 
        @PathVariable String category
    ) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @PostMapping
    @Operation(summary = "Создать новый товар")
    @ApiResponse(responseCode = "201", description = "Товар успешно создан")
    public ResponseEntity<Product> createProduct(
        @Parameter(description = "Данные нового товара", required = true) 
        @Valid @RequestBody Product product
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар")
    @ApiResponse(responseCode = "200", description = "Товар успешно обновлен")
    public ResponseEntity<Product> updateProduct(
        @Parameter(description = "ID товара", required = true) 
        @PathVariable Long id,
        @Parameter(description = "Данные обновленного товара", required = true) 
        @Valid @RequestBody Product productDetails
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар")
    @ApiResponse(responseCode = "204", description = "Товар успешно удален")
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "ID товара", required = true) 
        @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
