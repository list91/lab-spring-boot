package com.cardshop.controller;

import com.cardshop.model.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Контроллер товаров", description = "Операции с товарами")
public class ProductController {

    private List<Product> products = new ArrayList<>();

    @PostMapping
    @Operation(summary = "Создание нового товара")
    public Product createProduct(@RequestBody Product product) {
        product.setId(products.size() + 1L);
        products.add(product);
        return product;
    }

    @GetMapping
    @Operation(summary = "Получение списка всех товаров")
    public List<Product> getAllProducts() {
        return products;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение товара по ID")
    public Product getProductById(@PathVariable Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Инициализация тестовыми данными
    @PostMapping("/init")
    @Operation(summary = "Инициализация тестовыми товарами")
    public List<Product> initTestProducts() {
        products.clear();
        
        Product card1 = new Product();
        card1.setName("Магическая карта Дракона");
        card1.setDescription("Редкая карта с изображением древнего дракона");
        card1.setPrice(BigDecimal.valueOf(199.99));
        card1.setCategory("Magic: The Gathering");
        card1.setStockQuantity(5);
        
        Product card2 = new Product();
        card2.setName("Карта Эльфийского Воина");
        card2.setDescription("Легендарная карта эльфийского воителя");
        card2.setPrice(BigDecimal.valueOf(149.50));
        card2.setCategory("Fantasy Cards");
        card2.setStockQuantity(10);

        createProduct(card1);
        createProduct(card2);

        return products;
    }
}
