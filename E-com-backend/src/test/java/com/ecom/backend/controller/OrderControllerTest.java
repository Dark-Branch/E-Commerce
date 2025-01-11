package com.ecom.backend.controller;

import com.ecom.backend.model.Order;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.UserRepository;
import com.ecom.backend.service.OrderService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    private User user;

    @Test
    void testCreateOrderUsingValidUser() {
        this.user = new User();
        user.setUserName("testUserOrder");
        user.setPassword("password123");
        user.setRole("pakaya");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", user, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        this.user.setRole(null);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/auth/login", user, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        jwtToken = "Bearer " + loginResponse.getBody();

        Order order = new Order();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Order> request = new HttpEntity<>(order, headers);

        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/orders", request, Void.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertTrue(createResponse.getHeaders().containsKey("Location"));
        assertThat(createResponse.getHeaders().getLocation()).isNotNull();

    }
}

