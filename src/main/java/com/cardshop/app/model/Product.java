package com.cardshop.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название товара не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Positive(message = "Цена должна быть положительной")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "category")
    private String category;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;
}
