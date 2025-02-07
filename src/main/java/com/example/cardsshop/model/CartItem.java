package com.example.cardsshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Пользователь не может быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Товар не может быть пустым")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(value = 1, message = "Количество товара должно быть не менее 1")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    @PreUpdate
    public void preUpdate() {
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}
