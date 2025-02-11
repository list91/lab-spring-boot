package com.cardshop.service;

import com.cardshop.model.Product;
import com.cardshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> initTestProducts() {
        productRepository.deleteAll();

        Product card1 = new Product();
        card1.setName("Dragon Magic Card");
        card1.setDescription("Rare card with an ancient dragon image");
        card1.setPrice(BigDecimal.valueOf(199.99));
        card1.setCategory("Magic: The Gathering");
        card1.setStockQuantity(5);

        Product card2 = new Product();
        card2.setName("Elven Warrior Card");
        card2.setDescription("Legendary card of an elven warrior");
        card2.setPrice(BigDecimal.valueOf(149.50));
        card2.setCategory("Fantasy Cards");
        card2.setStockQuantity(10);

        productRepository.save(card1);
        productRepository.save(card2);

        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
