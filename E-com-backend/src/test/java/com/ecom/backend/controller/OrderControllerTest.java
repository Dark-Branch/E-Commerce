package com.ecom.backend.controller;

import com.ecom.backend.model.Order;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.OrderRepository;
import com.ecom.backend.repository.UserRepository;
import com.ecom.backend.service.AuthService;
import com.ecom.backend.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import static com.ecom.backend.testUtils.AuthUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private String baseUrl;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/orders";

        userRepository.deleteAll();
        orderRepository.deleteAll();

        user = saveUser(user, authService, "example@example.com");
        token = setupSignedUserAndGetToken(user, restTemplate);
    }

    @Test
    void testGetOrderById_ValidOrderAndAuthorizedUser_ReturnsOrder() {
        Order order = new Order();
        order.setUserName(user.getId());
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Order> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                request,
                Order.class,
                order.getId()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(order.getId(), response.getBody().getId());
    }

    @Test
    void testGetOrderById_ValidOrderButUnauthorizedUser_ThrowsUnauthorizedException() {
        // Create an order for a different user
        User anotherUser = new User();
        anotherUser.setUserName("anotherUser");
        anotherUser = userRepository.save(anotherUser);

        Order order = new Order();
        order.setUserName(anotherUser.getId());
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertThat(response.getBody()).contains("Order does not belong to user");
    }

    @Test
    void testGetOrderById_InvalidOrderId_ThrowsNotFoundException() {
        String invalidOrderId = "invalidOrderId";

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                request,
                String.class,
                invalidOrderId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody()).contains("Order Not Found");
    }

    @Test
    void testGetOrderById_MissingToken_ThrowsBadRequestException() {
        Order order = new Order();
        order.setUserName(user.getId());
        orderRepository.save(order);

        HttpEntity<Void> request = new HttpEntity<>(null); // No token

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetOrderById_InvalidToken_ThrowsUnauthorizedException() {
        Order order = new Order();
        order.setUserName(user.getId());
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken("invalidToken");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.GET,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testCreateOrderUsingValidUser() {
        Order order = new Order();

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Order> request = new HttpEntity<>(order, headers);

        ResponseEntity<Void> createResponse = restTemplate.postForEntity(baseUrl, request, Void.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertTrue(createResponse.getHeaders().containsKey("Location"));
        assertThat(createResponse.getHeaders().getLocation()).isNotNull();
    }


}
