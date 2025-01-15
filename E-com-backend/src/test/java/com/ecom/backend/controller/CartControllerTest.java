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
        Product product = new Product("Test Product", "Category", "SubCategory", 10.0);
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
    public void testAddItemToCartByUserId() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/add?userId={userId}",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getUserId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item added to cart");

        Cart updatedCart = cartRepository.findByUserId(existingCart.getUserId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(2);
    }

    @Test
    public void testAddExistingItemToCartByUserId() {
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        restTemplate.exchange(
                "/cart/add?userId={userId}",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getUserId()
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/add?userId={userId}",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getUserId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item added to cart");

        Cart updatedCart = cartRepository.findByUserId(existingCart.getUserId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(4); // Added twice
    }

    @Test
    public void testAddNonExistingProductToCart() {
        Cart.CartItem cartItem = new Cart.CartItem("Not an ID", 2, null, null);

        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/add?userId={userId}",
                HttpMethod.POST,
                request,
                String.class,
                existingCart.getUserId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Disabled   // TODO: implement session
    public void testAddItemToCartBySessionId() {
        // Prepare the cart item
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);

        // Prepare the request
        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);

        // Call the endpoint with a session ID
        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/add?sessionId={sessionId}",
                HttpMethod.POST,
                request,
                String.class,
                "testSessionId"
        );

        // Assert the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Item added to cart");

        // Verify the cart was updated
        Cart updatedCart = cartRepository.findBySessionId("testSessionId").orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);

        Cart.CartItem addedItem = updatedCart.getItems().get(0);
        assertThat(addedItem.getProductId()).isEqualTo(newProduct.getId());
        assertThat(addedItem.getQuantity()).isEqualTo(2);
    }

    @Test
    void testCheckoutCart() {
        CheckoutRequest checkoutRequest = new CheckoutRequest(existingCart.getId(), null, "Credit Card", null);

        // add item to cart
        Cart.CartItem cartItem = new Cart.CartItem(newProduct.getId(), 2, null, null);
        HttpEntity<Cart.CartItem> request = new HttpEntity<>(cartItem);
        ResponseEntity<Cart> cartResponse = restTemplate.exchange("/cart/{id}/addItem", HttpMethod.PATCH,
                request, Cart.class, existingCart.getId());
        assertThat(cartResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Order> response = restTemplate.postForEntity(
                "/cart/checkout",
                checkoutRequest,
                Order.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNull();

        ResponseEntity<Order> orderResponse = restTemplate.getForEntity(response.getHeaders().getLocation(), Order.class);

        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderResponse.getBody().getTotalAmount()).isEqualTo(newProduct.getPrice() * 2);
        assertThat(orderResponse.getBody().getPaymentMethod()).isEqualTo("Credit Card");
        assertThat(orderResponse.getBody().getStatus()).isEqualTo("Pending");
        assertThat(orderResponse.getBody().getItems().size()).isEqualTo(1);
        assertThat(orderResponse.getBody().getItems().get(0).getProductId()).isEqualTo(newProduct.getId());
    }

}
