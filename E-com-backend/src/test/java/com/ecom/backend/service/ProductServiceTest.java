package com.ecom.backend.service;


import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setId("123456");
        product.setName("Wireless Bluetooth Headphones");
        product.setPrice(59.99);
        product.setRating(4.5);
        product.setMainImg("https://example.com/images/headphones-main.jpg");
        product.setInventoryCount(15);

        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);
        assertEquals("123456", createdProduct.getId());
        assertEquals("Wireless Bluetooth Headphones", createdProduct.getName());
    }
}
