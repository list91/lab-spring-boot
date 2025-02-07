package com.example.cardsshop.service;

import com.example.cardsshop.model.Category;
import com.example.cardsshop.model.Product;
import com.example.cardsshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Тестовая категория", "Описание категории");
        testProduct = new Product("Тестовый товар", "Описание товара", 
                                  BigDecimal.valueOf(100.50), testCategory);
        testProduct.setId(1L);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product createdProduct = productService.createProduct(testProduct);

        // Assert
        assertNotNull(createdProduct);
        assertEquals(testProduct.getName(), createdProduct.getName());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> foundProduct = productService.getProductById(1L);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(testProduct.getId(), foundProduct.get().getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        // Act
        List<Product> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(testProduct.getName(), products.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Product updatedProductData = new Product("Обновленный товар", "Новое описание", 
                                                 BigDecimal.valueOf(150.75), testCategory);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProductData);

        // Act
        Product updatedProduct = productService.updateProduct(1L, updatedProductData);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("Обновленный товар", updatedProduct.getName());
        assertEquals(BigDecimal.valueOf(150.75), updatedProduct.getPrice());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        when(productRepository.findByCategory_Name("Тестовая категория"))
            .thenReturn(List.of(testProduct));

        // Act
        List<Product> categoryProducts = productService.getProductsByCategory("Тестовая категория");

        // Assert
        assertNotNull(categoryProducts);
        assertFalse(categoryProducts.isEmpty());
        assertEquals(1, categoryProducts.size());
        assertEquals(testProduct.getName(), categoryProducts.get(0).getName());
        verify(productRepository, times(1)).findByCategory_Name("Тестовая категория");
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productService.getProductById(999L);

        // Assert
        assertTrue(foundProduct.isEmpty());
        verify(productRepository, times(1)).findById(999L);
    }
}
