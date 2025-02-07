package com.example.cardsshop.repository;

import com.example.cardsshop.model.CartItem;
import com.example.cardsshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Найти все товары в корзине для конкретного пользователя
    List<CartItem> findByUser_Id(Long userId);

    // Найти конкретный товар в корзине пользователя
    Optional<CartItem> findByUser_IdAndProduct_Id(Long userId, Long productId);

    // Удалить все товары из корзины пользователя
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    // Подсчет количества товаров в корзине пользователя
    long countByUser_Id(Long userId);

    // Проверка наличия товара в корзине пользователя
    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);
}
