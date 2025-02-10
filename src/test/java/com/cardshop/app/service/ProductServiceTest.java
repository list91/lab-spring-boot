package com.cardshop.app.service;

import com.cardshop.app.model.Product;
import com.cardshop.app.repository.ProductRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Card");
        testProduct.setPrice(BigDecimal.valueOf(10.99));
        testProduct.setCategory("Magic");
        testProduct.setStockQuantity(100);
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> products = productService.getAllProducts();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals(testProduct, products.get(0));
    }

    @Test
    void getProductById_ExistingProduct_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> product = productService.getProductById(1L);

        assertTrue(product.isPresent());
        assertEquals(testProduct, product.get());
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product createdProduct = productService.createProduct(testProduct);

        assertNotNull(createdProduct);
        assertEquals(testProduct, createdProduct);
        verify(productRepository).save(testProduct);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Card");
        updatedDetails.setPrice(BigDecimal.valueOf(15.99));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product updatedProduct = productService.updateProduct(1L, updatedDetails);

        assertEquals("Updated Card", updatedProduct.getName());
        assertEquals(BigDecimal.valueOf(15.99), updatedProduct.getPrice());
        verify(productRepository).save(testProduct);
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        productService.deleteProduct(1L);

        verify(productRepository).delete(testProduct);
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        when(productRepository.findByCategory("Magic")).thenReturn(List.of(testProduct));

        List<Product> products = productService.getProductsByCategory("Magic");

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
        assertEquals("Magic", products.get(0).getCategory());
    }
}
