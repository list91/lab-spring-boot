package com.example.cardsshop.integration;

import com.example.cardsshop.model.Category;
import com.example.cardsshop.model.User;
import com.example.cardsshop.model.enums.Role;
import com.example.cardsshop.repository.CategoryRepository;
import com.example.cardsshop.repository.UserRepository;
import com.example.cardsshop.service.CategoryService;
import com.example.cardsshop.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserCategoryIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Очистка базы данных перед каждым тестом
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        // Создание тестового пользователя
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("rawPassword");
        testUser.setRole(Role.USER);
        testUser.setEnabled(true);

        // Создание тестовой категории
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testUserCreationAndCategoryManagement() {
        // Создание пользователя
        User createdUser = userService.createUser(testUser);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertTrue(passwordEncoder.matches("rawPassword", createdUser.getPassword()));

        // Создание категории
        Category createdCategory = categoryService.createCategory(testCategory);
        assertNotNull(createdCategory);
        assertNotNull(createdCategory.getId());
        assertEquals("Test Category", createdCategory.getName());

        // Проверка списка пользователей
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(createdUser.getId(), users.get(0).getId());

        // Проверка списка категорий
        List<Category> categories = categoryService.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals(createdCategory.getId(), categories.get(0).getId());

        // Обновление пользователя
        createdUser.setUsername("updateduser");
        User updatedUser = userService.updateUser(createdUser.getId(), createdUser);
        assertEquals("updateduser", updatedUser.getUsername());

        // Обновление категории
        createdCategory.setDescription("Updated Description");
        Category updatedCategory = categoryService.updateCategory(createdCategory.getId(), createdCategory);
        assertEquals("Updated Description", updatedCategory.getDescription());

        // Поиск пользователя и категории по ID
        Optional<User> foundUser = userService.findUserById(createdUser.getId());
        Optional<Category> foundCategory = categoryService.getCategoryById(createdCategory.getId());

        assertTrue(foundUser.isPresent());
        assertTrue(foundCategory.isPresent());
        assertEquals(updatedUser.getId(), foundUser.get().getId());
        assertEquals(updatedCategory.getId(), foundCategory.get().getId());

        // Удаление пользователя и категории
        userService.deleteUser(createdUser.getId());
        categoryService.deleteCategory(createdCategory.getId());

        // Проверка, что пользователь и категория удалены
        assertTrue(userService.findUserById(createdUser.getId()).isEmpty());
        assertTrue(categoryService.getCategoryById(createdCategory.getId()).isEmpty());
    }

    @Test
    void testUniqueConstraintsAndValidation() {
        // Создание пользователя с уникальным email
        userService.createUser(testUser);

        // Попытка создания пользователя с тем же email должна вызвать исключение
        assertThrows(IllegalArgumentException.class, () -> {
            User duplicateUser = new User();
            duplicateUser.setUsername("anotheruser");
            duplicateUser.setEmail("test@example.com");
            duplicateUser.setPassword("anotherPassword");
            userService.createUser(duplicateUser);
        });

        // Создание категории
        Category category1 = new Category();
        category1.setName("Category 1");
        categoryService.createCategory(category1);

        // Создание категории с тем же именем не должно вызывать ошибку
        Category category2 = new Category();
        category2.setName("Category 2");
        Category createdCategory = categoryService.createCategory(category2);
        assertNotNull(createdCategory);
    }
}
