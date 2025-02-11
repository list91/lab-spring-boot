package com.cardshop.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Schema(description = "Модель корзины")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор корзины")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь, которому принадлежит корзина")
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @Schema(description = "Элементы в корзине")
    private List<CartItem> items = new ArrayList<>();

    @Schema(description = "Общая стоимость товаров в корзине")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // Вложенный класс для элементов корзины
    @Embeddable
    public static class CartItem {
        @ManyToOne
        @JoinColumn(name = "product_id")
        @Schema(description = "Товар в корзине")
        private Product product;

        @Column(nullable = false)
        @Schema(description = "Количество товара", example = "2")
        private Integer quantity;

        // Геттеры и сеттеры
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    // Методы для работы с корзиной
    public void addItem(Product product, Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        items.add(cartItem);
        updateTotalPrice();
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().getId().equals(product.getId()));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        totalPrice = items.stream()
            .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
