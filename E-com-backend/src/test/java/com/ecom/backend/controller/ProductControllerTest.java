package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; // Autowired MockMvc for controller testing

    @MockitoBean
    private ProductService productService; // Mocked ProductService to be injected into the controller

    @Autowired
    private ObjectMapper objectMapper; // Autowired ObjectMapper for converting objects to JSON

    @Test
    @Disabled
    void testCreateProduct() throws Exception {
        // Given: Create a sample product
        Product product = new Product();
        product.setId("123456");
        product.setName("Wireless Bluetooth Headphones");
        product.setPrice(59.99);
        product.setRating(4.5);
        product.setMainImg("https://example.com/images/headphones-main.jpg");
        product.setInventoryCount(15);

        // Mock the service method (what happens when the controller calls it)
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        // When and Then: Perform the POST request to the endpoint
        mockMvc.perform(post("/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(product))) // Convert product to JSON
                .andExpect(status().isCreated()) // Expect a 201 Created status
                .andExpect(jsonPath("$.id").value("123456")) // Check the ID in the response
                .andExpect(jsonPath("$.name").value("Wireless Bluetooth Headphones")) // Check name
                .andExpect(jsonPath("$.price").value(59.99)); // Check price
    }
}
