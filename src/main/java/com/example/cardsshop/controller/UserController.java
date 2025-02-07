package com.example.cardsshop.controller;

import com.example.cardsshop.model.User;
import com.example.cardsshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает полный список пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<User> getUserById(
        @Parameter(description = "Уникальный идентификатор пользователя", required = true)
        @PathVariable Long id
    ) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя с уникальным email")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка при создании пользователя")
    public ResponseEntity<User> createUser(
        @Parameter(description = "Данные нового пользователя", required = true)
        @Valid @RequestBody User user
    ) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о пользователе", description = "Обновляет данные существующего пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<User> updateUser(
        @Parameter(description = "Уникальный идентификатор пользователя", required = true)
        @PathVariable Long id,
        @Parameter(description = "Новые данные пользователя", required = true)
        @Valid @RequestBody User userDetails
    ) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его уникальному идентификатору")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "Уникальный идентификатор пользователя", required = true)
        @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
