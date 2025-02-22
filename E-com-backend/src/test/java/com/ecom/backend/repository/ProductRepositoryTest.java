package com.ecom.backend.repository;

import com.ecom.backend.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setId("23");
        product.setName("Wireless Bluetooth Headphones");
        product.setPrice(59.99);
        product.setRating(4.5);
        product.setMainImg("https://example.com/images/headphones-main.jpg");
        product.setInventoryCount(15);

        productRepository.save(product);

        Product savedProduct = productRepository.findById("23").orElse(null);
        assertEquals("Wireless Bluetooth Headphones", savedProduct.getName());
        assertEquals(59.99, savedProduct.getPrice());
        assertEquals(4.5, savedProduct.getRating());
    }
}
