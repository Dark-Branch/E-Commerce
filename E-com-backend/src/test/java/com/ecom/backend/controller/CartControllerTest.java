package com.ecom.backend.controller;

import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Product;
import com.ecom.backend.repository.CartRepository;
import com.ecom.backend.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    private Cart existingCart;
    private Product newProduct;

    @BeforeEach
    void setUp() {
        Product product = new Product("Test Product", "Category", "SubCategory", 10.0);
        product.setInventoryCount(20);
        newProduct = productRepository.save(product);

        existingCart = new Cart(null, "testUserId", new ArrayList<>());
        existingCart = cartRepository.save(existingCart);
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void testAddItemToCartByCartId() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<Cart> response = restTemplate.exchange("/cart/{id}/addItem", HttpMethod.PATCH,
                request, Cart.class, existingCart.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Cart updatedCart = response.getBody();
        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(2);
    }

    @Test
    public void testAddExistingItemToCartByCartId() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<Cart> response = restTemplate.exchange("/cart/{id}/addItem", HttpMethod.PATCH,
                request, Cart.class, existingCart.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = restTemplate.exchange("/cart/{id}/addItem", HttpMethod.PATCH,
                request, Cart.class, existingCart.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Cart updatedCart = response.getBody();
        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        // added two times
        assertThat(addedItem.getQuantity()).isEqualTo(4);
    }

    @Test
    public void testAddNonExistingProductToCart() {
        Cart.CartItem cartItem = new Cart.CartItem("Not an ID", 2, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<String> response = restTemplate.exchange("/cart/{id}/addItem", HttpMethod.PATCH,
                request, String.class, existingCart.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    

}
