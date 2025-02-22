package com.ecom.backend.controller;

import com.ecom.backend.DTO.CheckoutRequest;
import com.ecom.backend.model.Cart;
import com.ecom.backend.model.Order;
import com.ecom.backend.model.Product;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.CartRepository;
import com.ecom.backend.repository.ProductRepository;
import com.ecom.backend.repository.UserRepository;
import com.ecom.backend.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ecom.backend.testUtils.AuthUtils.*;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private Cart existingCart;
    private Product newProduct;
    private String baseUrl;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/cart";

        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        user = saveUser(user, authService, "example@example.com");
        token = setupSignedUserAndGetToken(user, restTemplate);

        Product product = new Product("Test Product", "Category", "SubCategory", 10.0,"0008", 5);
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

    @Test
    void checkoutCart_SuccessfulCheckout_ReturnsOrder() {
        // make checkout request, get order id, then send order request to confirm
        Cart.CartItem cartItem1 = new Cart.CartItem(newProduct.getId(), 2, "v1", newProduct.getPrice());
        existingCart.getItems().add(cartItem1);
        cartRepository.save(existingCart);

        CheckoutRequest checkoutRequest = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 1, "v1", newProduct.getPrice()))
        );

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<CheckoutRequest> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/checkout",
                HttpMethod.POST,
                request,
                Void.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey("Location"));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        Cart updatedCart = cartRepository.findById(existingCart.getId()).orElseThrow();
        assertThat(updatedCart.getItems()).hasSize(1);
        assertThat(updatedCart.getItems().get(0).getQuantity()).isEqualTo(1); // Remaining quantity

        URI orderLocation = response.getHeaders().getLocation();
        assertThat(orderLocation).isNotNull();

        HttpEntity<CheckoutRequest> getRequest = new HttpEntity<>(headers);

        System.out.println(orderLocation);
        ResponseEntity<Order> orderResponse = restTemplate.exchange(
                orderLocation,
                HttpMethod.GET,
                getRequest,
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
        CheckoutRequest checkoutRequest = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of() // No items selected
        );

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<CheckoutRequest> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/checkout",
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("No items selected for checkout");
    }

    @Test
    void checkoutCart_ProductNotFoundInCart_ThrowsNotFoundException() {
        CheckoutRequest checkoutRequest = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem("nonExistentProductId", 1, "v1", 10.0))
        );

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<CheckoutRequest> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/checkout",
                HttpMethod.POST,
                request,
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

        CheckoutRequest checkoutRequest = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 3, "v1", newProduct.getPrice()))
        );

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<CheckoutRequest> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/checkout",
                HttpMethod.POST,
                request,
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

        CheckoutRequest checkoutRequest = new CheckoutRequest(
                existingCart.getId(),
                "123 Main St, City, Country",
                "Credit Card",
                "Leave at the door",
                List.of(new Cart.CartItem(newProduct.getId(), 1, "v1", newProduct.getPrice()))
        );

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<CheckoutRequest> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/checkout",
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Product " + newProduct.getId() + " is out of stock or quantity exceeds available stock");
    }
}
