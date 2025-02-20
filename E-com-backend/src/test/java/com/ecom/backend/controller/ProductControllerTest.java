package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import com.ecom.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ProductRepository productRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/products";
        productRepository.deleteAll();
    }

    @Test
//    @Disabled
    void testCreateProduct() throws Exception {
        // create product, then check if it is created
        Product product = new Product();
        product.setName("Wireless Bluetooth Headphones");
        product.setPrice(59.99);
        product.setRating(4.5);
        product.setMainImg("https://example.com/images/headphones-main.jpg");
        product.setInventoryCount(15);

        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, product, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        URI location = headers.getLocation();
        assertNotNull(location, "Location header should not be null");

        ResponseEntity<Product> getResponse = restTemplate.getForEntity(location, Product.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Wireless Bluetooth Headphones", getResponse.getBody().getName());
        assertEquals(59.99, getResponse.getBody().getPrice());
    }

    @Test
    void testGetProductByCategory() {
        Product product = Product.builder()
                .id("1")
                .name("Smartphone")
                .price(599.99)
                .rating(4.5)
                .category("electronics")
                .subCategory("mobile")
                .inventoryCount(50)
                .mainImg("image_url")
                .build();
        productRepository.save(product);

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                baseUrl + "/category/electronics?page=0&limit=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Smartphone", response.getBody().get(0).getName());
    }

    @Test
    void testSearchProductsUsingName() {
        Product product1 = Product.builder()
                .id("1")
                .name("Smartphone")
                .price(599.99)
                .rating(4.5)
                .category("electronics")
                .subCategory("mobile")
                .inventoryCount(50)
                .mainImg("image_url")
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Laptop")
                .price(999.99)
                .rating(4.7)
                .category("electronics")
                .subCategory("computers")
                .inventoryCount(20)
                .mainImg("image_url")
                .build();

        productRepository.saveAll(List.of(product1, product2));

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                baseUrl + "/search?name=Smartphone",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchProducts() {
        Product product1 = Product.builder()
                .id("1")
                .name("Smartphone")
                .price(599.99)
                .rating(4.5)
                .category("electronics")
                .subCategory("mobile")
                .inventoryCount(50)
                .mainImg("image_url")
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Laptop")
                .price(999.99)
                .rating(4.7)
                .category("electronics")
                .subCategory("computers")
                .inventoryCount(20)
                .mainImg("image_url")
                .build();

        productRepository.saveAll(List.of(product1, product2));

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                baseUrl + "/search?name=Smartphone&category=electronics&minPrice=500&maxPrice=600&sortBy=name&sortOrder=asc",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchProductsUsingPrice() {
        Product product1 = Product.builder()
                .id("1")
                .name("Smartphone")
                .price(599.99)
                .rating(4.5)
                .category("electronics")
                .subCategory("mobile")
                .inventoryCount(50)
                .mainImg("image_url")
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("Laptop")
                .price(999.99)
                .rating(4.7)
                .category("electronics")
                .subCategory("computers")
                .inventoryCount(20)
                .mainImg("image_url")
                .build();

        productRepository.saveAll(List.of(product1, product2));

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                baseUrl + "/search?minPrice=500&maxPrice=600",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}