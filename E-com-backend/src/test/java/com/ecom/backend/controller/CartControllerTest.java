package com.ecom.backend.controller;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Order;
import com.ecom.backend.model.Product;
import com.ecom.backend.repository.CartRepository;
import com.ecom.backend.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;

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
        Product product = new Product("Test Product", "Category", "SubCategory", 10.0, "0006" , 9);
        product.setInventoryCount(20);
        newProduct = productRepository.save(product);

        Date date = new Date();
        existingCart = Cart.builder()
                .userId("testUserId")
                .active(true)
                .items(new ArrayList<>())
                .createdAt(date)
                .updatedAt(date)
                .build();
        existingCart = cartRepository.save(existingCart);
    }

    @AfterEach
    void tearDown() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void AddItemToCart() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/add",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item added to cart");

        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(2);
    }

    @Test
    public void AddExistingItemToCart() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        restTemplate.exchange(
                "/cart/{cartId}/add",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getId()
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/add",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item added to cart");

        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(4); // Added twice
    }

    @Test
    public void AddNonExistingProductToCart_ThrowsNotFoundException() {
        Cart.CartItem cartItem = new Cart.CartItem("Not an ID", 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/add",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Disabled
    void getCart_WithNonExistentCartId_ThrowsNotFoundException() {
        String nonExistentCartId = "nonExistentCartId";

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}",
                HttpMethod.GET,
                null,
                String.class,
                nonExistentCartId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Cart not found");
    }

    @Test
    void getCart_WithUserId_ReturnsCart() {
        String userId = existingCart.getUserId();

        ResponseEntity<Cart> response = restTemplate.exchange(
                "/cart?userId=" + userId,
                HttpMethod.GET,
                null,
                Cart.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(userId);
    }

    @Test
    void getCart_WithSessionId_ReturnsCart() {
        String sessionId = "testSessionId";
        Cart guestCart = Cart.builder()
                .sessionId(sessionId)
                .active(true)
                .items(new ArrayList<>())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        cartRepository.save(guestCart);

        ResponseEntity<Cart> response = restTemplate.exchange(
                "/cart?sessionId=" + sessionId,
                HttpMethod.GET,
                null,
                Cart.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSessionId()).isEqualTo(sessionId);
    }

    @Test
    void getCart_WithNeitherUserIdNorSessionId_ReturnsBadRequest() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/cart",
                HttpMethod.GET,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Either userId or sessionId must be provided");
    }

    @Test
    void getCart_WithNonExistentUserId_CreatesNewCart() {
        String newUserId = "newUserId";

        ResponseEntity<Cart> response = restTemplate.exchange(
                "/cart?userId=" + newUserId,
                HttpMethod.GET,
                null,
                Cart.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo(newUserId);
        assertThat(response.getBody().getItems()).isEmpty();
    }

    @Test
    void getCart_WithNonExistentSessionId_CreatesNewCart() {
        String newSessionId = "newSessionId";

        ResponseEntity<Cart> response = restTemplate.exchange(
                "/cart?sessionId=" + newSessionId,
                HttpMethod.GET,
                null,
                Cart.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSessionId()).isEqualTo(newSessionId);
        assertThat(response.getBody().getItems()).isEmpty();
    }

    @Test
    void removeItemFromCart_RemovesItem() {
        Cart.CartItem item = new Cart.CartItem(newProduct.getId(), 1, null, newProduct.getPrice());
        existingCart.getItems().add(item);
        cartRepository.save(existingCart);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/remove?productId={productId}",
                HttpMethod.POST,
                null,
                String.class,
                existingCart.getId(),
                newProduct.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item removed from cart");

        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).isEmpty();
    }

    @Test
    @Disabled
    void removeItemFromCart_WithSessionId_RemovesItem() {
        String sessionId = "testSessionId";
        Cart guestCart = Cart.builder()
                .sessionId(sessionId)
                .active(true)
                .items(new ArrayList<>())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Cart.CartItem item = new Cart.CartItem(newProduct.getId(), 1, null, newProduct.getPrice());
        guestCart.getItems().add(item);
        cartRepository.save(guestCart);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/remove?sessionId=" + sessionId + "&productId=" + newProduct.getId(),
                HttpMethod.POST,
                null,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item removed from cart");

        Cart updatedCart = cartRepository.findById(guestCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).isEmpty();
    }

    @Test
    void removeItemFromCart_WithNonExistentProductId_ThrowsNotFoundException() {
        String nonExistentProductId = "nonExistentProductId";

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/remove?productId={productId}",
                HttpMethod.POST,
                null,
                String.class,
                existingCart.getId(),
                nonExistentProductId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Item not found in cart");
    }

    @Test
    void removeItemFromCart_WithNonExistentCartId_ThrowsNotFoundException() {
        String nonExistentCartId = "nonExistentCartId";
        String productId = newProduct.getId();

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}/remove?productId={productId}",
                HttpMethod.POST,
                null,
                String.class,
                nonExistentCartId,
                productId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Cart not found");
    }

    @Test
    void clearCart_ClearsCart() {
        Cart.CartItem item = new Cart.CartItem(newProduct.getId(), 1, null, newProduct.getPrice());
        existingCart.getItems().add(item);
        cartRepository.save(existingCart);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/{cartId}",
                HttpMethod.DELETE,
                null,
                String.class,
                existingCart.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Cart cleared");

        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).isEmpty();
    }

}
