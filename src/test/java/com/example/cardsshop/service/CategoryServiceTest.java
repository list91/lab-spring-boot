package com.example.cardsshop.service;

import com.example.cardsshop.model.Category;
import com.example.cardsshop.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory, new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> allCategories = categoryService.getAllCategories();

        // Assert
        assertEquals(2, allCategories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        Optional<Category> foundCategory = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(foundCategory.isPresent());
        assertEquals(testCategory.getId(), foundCategory.get().getId());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Category> foundCategory = categoryService.getCategoryById(999L);

        // Assert
        assertTrue(foundCategory.isEmpty());
        verify(categoryRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateCategory() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        Category createdCategory = categoryService.createCategory(testCategory);

        // Assert
        assertNotNull(createdCategory);
        assertEquals(testCategory.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(testCategory);
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Old Category");
        existingCategory.setDescription("Old Description");

        Category updatedCategoryDetails = new Category();
        updatedCategoryDetails.setName("New Category");
        updatedCategoryDetails.setDescription("New Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // Act
        Category updatedCategory = categoryService.updateCategory(1L, updatedCategoryDetails);

        // Assert
        verify(categoryRepository, times(1)).save(existingCategory);
        assertEquals("New Category", updatedCategory.getName());
        assertEquals("New Description", updatedCategory.getDescription());
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Arrange
        Category updatedCategoryDetails = new Category();
        updatedCategoryDetails.setName("New Category");
        updatedCategoryDetails.setDescription("New Description");

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(999L, updatedCategoryDetails));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    void testDeleteCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(999L));
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
