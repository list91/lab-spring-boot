package com.cardshop.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Entity
@Table(name = "users")
@Schema(description = "Модель пользователя")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Логин пользователя", example = "john_doe")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Зашифрованный пароль")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Электронная почта", example = "john@example.com")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Schema(description = "Роли пользователя")
    private Set<String> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Корзина пользователя")
    private Cart cart;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
}
