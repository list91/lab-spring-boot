package com.cardshop.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Schema(description = "Модель товара")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор товара")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Название товара", example = "Коллекционная карточка")
    private String name;

    @Column(length = 1000)
    @Schema(description = "Описание товара")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Цена товара", example = "99.99")
    private BigDecimal price;

    @Column(nullable = false)
    @Schema(description = "Категория товара", example = "Magic: The Gathering")
    private String category;

    @Column(nullable = false)
    @Schema(description = "Количество товара на складе", example = "10")
    private Integer stockQuantity;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}
