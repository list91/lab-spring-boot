package com.example.cardsshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9_-]+$", message = "Имя пользователя может содержать только буквы, цифры, подчеркивания и дефисы")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 255, message = "Пароль должен содержать от 8 до 255 символов")
    @Pattern.List({
        @Pattern(regexp = ".*[A-Z].*", message = "Пароль должен содержать хотя бы одну заглавную букву"),
        @Pattern(regexp = ".*[a-z].*", message = "Пароль должен содержать хотя бы одну строчную букву"),
        @Pattern(regexp = ".*\\d.*", message = "Пароль должен содержать хотя бы одну цифру"),
        @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*", message = "Пароль должен содержать хотя бы один специальный символ")
    })
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean enabled = true;

    public enum Role {
        USER, ADMIN
    }

    // Конструкторы
    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Методы UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
