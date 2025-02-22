package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        productRepository.saveAll(List.of(
                new Product("Product 1", "Electronics", "Mobile", 100.0, "0001", 5),
                new Product("Product 2", "Electronics", "Laptop", 500.0, "0002", 5),
                new Product("Product 3", "Clothing", "Men", 50.0, "0003", 5)
        ));
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
        productRepository.deleteAll();
        
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

    @Test
    public void shouldCreteNewProduct() {
        Product product = new Product();
        List<String> tags = Arrays.asList("Electronics", "Fan", "Speed");

        product.setName("alutheka");
        product.setCategory("Electronics");
        product.setSubCategory("Fan");
        product.setPrice(100);
        product.setInventoryCount(6);
        product.setSellerId("0005");
        product.setTags(tags);


        ResponseEntity<Void> response = restTemplate.postForEntity("/api/products", product, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI productLocation = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .getForEntity(productLocation, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        System.out.println(getResponse.getBody());
        String id = documentContext.read("$.id");
        String name = documentContext.read("$.name");
        String category = documentContext.read("$.category");
        String subCategory = documentContext.read("$.subCategory");
        Double price = documentContext.read("$.price");
        String sellerId = documentContext.read("$.sellerId");
        int inventoryCount = documentContext.read("$.inventoryCount");
        List<String> responseTags = documentContext.read("$.tags");




        assertThat(id).isNotNull();
        assertThat(name).isEqualTo("alutheka");
        assertThat(category).isEqualTo("Electronics");
        assertThat(subCategory).isEqualTo("Fan");
        assertThat(price).isEqualTo(100.0);
        assertThat(sellerId).isEqualTo("0005");
        assertThat(inventoryCount).isEqualTo(6);
        assertThat(responseTags).containsExactly("Electronics", "Fan", "Speed");
        //These assertThat statements are part of the assertions in your test.
        // Assertions are used to verify that the actual output of your code
        // (in this case, the values returned from the API response) matches the expected values.
        // They help ensure that your code behaves correctly by checking that the data returned by the API (e.g., product details) is accurate.
    }

    @Test
    void testGetProductByCategoryWithoutSubCategory() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                baseUrl + "/category/Electronics?page=0&limit=10", Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getName()).isEqualTo("Product 1");
        assertThat(response.getBody()[1].getName()).isEqualTo("Product 2");
    }

    @Test
    @Disabled
        // TODO
    void testGetProductByCategoryWithSubCategory() {
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                baseUrl + "/category/Electronics?subCategory=Mobile&page=0&limit=10", Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("Product 1");
    }

    @Test
    void testGetProductByCategoryInvalidPageOrLimit() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/category/Electronics?page=-1&limit=0", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Page must be >= 0 and limit > 0");
    }

    @Test
    public void testUpdateProductSuccess() throws Exception {
        // i know this will succeed because of previous test
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                baseUrl + "/category/Electronics?page=0&limit=10", Product[].class);
        assertThat(response.getBody()).isNotNull();

        Product product = response.getBody()[0];
        product.setPrice(63);

        HttpEntity<Product> request = new HttpEntity<>(product);

        ResponseEntity<Void> putResponse = restTemplate.exchange(
                baseUrl + "/{id}", HttpMethod.PUT, request, Void.class, product.getId());

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Product> getResponse = restTemplate.getForEntity(
                baseUrl + "/{id}", Product.class, product.getId());
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Product updatedProduct = getResponse.getBody();
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());
        assertThat(updatedProduct.getPrice()).isEqualTo(63);
    }

    @Test
    public void testUpdateProductNotFound() throws Exception {
        Product product = new Product();
        product.setId("non-existent-id");
        product.setName("Non-Existent Product");
        product.setPrice(100.0);

        HttpEntity<Product> request = new HttpEntity<>(product);

        ResponseEntity<Void> putResponse = restTemplate.exchange(
                baseUrl + "/{id}", HttpMethod.PUT, request, Void.class, product.getId());

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}