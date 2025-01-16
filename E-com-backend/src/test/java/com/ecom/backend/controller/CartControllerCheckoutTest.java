package com.ecom.backend.controller;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Order;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerCheckoutTest {

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
    void checkoutCart_SuccessfulCheckout_ReturnsOrder() {
        // make checkout request, get order id, then send order request to confirm
        Cart.CartItem cartItem1 = new Cart.CartItem(newProduct.getId(), 2, "v1", newProduct.getPrice());
        existingCart.getItems().add(cartItem1);
        cartRepository.save(existingCart);

        CheckoutRequest request = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 1, "v1", newProduct.getPrice()))
        );

        ResponseEntity<Void> response = restTemplate.exchange(
                "/cart/checkout",
                HttpMethod.POST,
                new HttpEntity<>(request),
                Void.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey("Location"));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        // confirm cart
        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);
        assertThat(updatedCart.getItems().get(0).getQuantity()).isEqualTo(1); // Remaining quantity

        // confirm order is placed by sending a request
        URI orderLocation = response.getHeaders().getLocation();
        assertThat(orderLocation).isNotNull();

        ResponseEntity<Order> orderResponse = restTemplate.exchange(
                orderLocation,
                HttpMethod.GET,
                null,
                Order.class
        );

        assertThat(orderResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(orderResponse.getBody()).isNotNull();
        assertThat(orderResponse.getBody().getItems()).hasSize(1);
        assertThat(orderResponse.getBody().getItems().get(0).getProductId()).isEqualTo(newProduct.getId());
        assertThat(orderResponse.getBody().getItems().get(0).getQuantity()).isEqualTo(1);
    }

    @Test
    void checkoutCart_NoItemsSelected_ThrowsBadRequest() {
        CheckoutRequest request = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of() // No items selected
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/checkout",
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("No items selected for checkout");
    }

    @Test
    void checkoutCart_ProductNotFoundInCart_ThrowsNotFoundException() {
        CheckoutRequest request = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem("nonExistentProductId", 1, "v1", 10.0))
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/checkout",
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Product nonExistentProductId not found in cart");
    }

    @Test
    void checkoutCart_QuantityExceedsCartQuantity_ThrowsBadRequest() {
        Cart.CartItem cartItem1 = new Cart.CartItem(newProduct.getId(), 2, "v1", newProduct.getPrice());
        existingCart.getItems().add(cartItem1);
        cartRepository.save(existingCart);

        CheckoutRequest request = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 3, "v1", newProduct.getPrice()))
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/checkout",
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Requested quantity for product " + newProduct.getId() + " exceeds available quantity in cart");
    }

    @Test
    void checkoutCart_ProductOutOfStock_ThrowsRuntimeException() {
        // Arrange: Add items to the cart
        Cart.CartItem cartItem1 = new Cart.CartItem(newProduct.getId(), 2, "v1", newProduct.getPrice());
        existingCart.getItems().add(cartItem1);
        cartRepository.save(existingCart);

        // Set product inventory to 0
        newProduct.setInventoryCount(0);
        productRepository.save(newProduct);

        CheckoutRequest request = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 1, "v1", newProduct.getPrice()))
        );

        ResponseEntity<String> response = restTemplate.exchange(
                "/cart/checkout",
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Product " + newProduct.getId() + " is out of stock or quantity exceeds available stock");
    }
}
