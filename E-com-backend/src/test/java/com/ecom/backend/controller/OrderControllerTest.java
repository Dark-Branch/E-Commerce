package com.ecom.backend.controller;

import com.ecom.backend.model.Order;
import com.ecom.backend.model.User;
import com.ecom.backend.repository.OrderRepository;
import com.ecom.backend.repository.UserRepository;
import com.ecom.backend.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.List;

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
        order.setUserId(user.getId());
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
        order.setUserId(anotherUser.getId());
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
        assertThat(response.getBody()).contains("Order not found");
    }

    @Test
    @Disabled
    // here. the problem will be when there is a different name than jwt token
    // app will be recognized it as another endpoint
    void testGetOrderById_MissingToken_ThrowsBadRequestException() {
        Order order = new Order();
        order.setUserId(user.getId());
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
        order.setUserId(user.getId());
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
    void testGetOrderHistory_UserHasOrders_ReturnsOrders() {
        Order order1 = new Order();
        order1.setUserId(user.getId());
        order1.setStatus("Confirmed");
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUserId(user.getId());
        order2.setStatus("Pending");
        orderRepository.save(order2);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Order>> response = restTemplate.exchange(
                baseUrl + "/history",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Order>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetOrderHistory_UserHasNoOrders_ReturnsEmptyList() {
        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Order>> response = restTemplate.exchange(
                baseUrl + "/history",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Order>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetOrderHistory_MissingToken_ThrowsForbiddenException() {
        HttpEntity<Void> request = new HttpEntity<>(null); // No token

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/history",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testGetOrderHistory_InvalidToken_ThrowsUnauthorizedException() {
        HttpHeaders headers = getHttpHeadersWithToken("invalidToken");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/history",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetOrderHistory_FiltersOutCancelledOrders_ReturnsActiveOrders() {
        Order order1 = new Order();
        order1.setUserId(user.getId());
        order1.setStatus("Confirmed");
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUserId(user.getId());
        order2.setStatus("Cancelled");
        orderRepository.save(order2);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<Order>> response = restTemplate.exchange(
                baseUrl + "/history",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Order>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Confirmed", response.getBody().get(0).getStatus());
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

    @Test
    void testConfirmOrder_PendingOrder_ConfirmsOrder() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                Void.class,
                order.getId()
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals("Confirmed", updatedOrder.getStatus());
    }

    @Test
    void testConfirmOrder_NonPendingOrder_ThrowsException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Confirmed");
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody()).contains("Order cannot be confirmed");
    }

    @Test
    void testConfirmOrder_OrderDoesNotBelongToUser_ThrowsUnauthorizedException() {
        User anotherUser = new User();
        anotherUser.setUserName("anotherUser");
        anotherUser = userRepository.save(anotherUser);

        Order order = new Order();
        order.setUserId(anotherUser.getId());
        order.setStatus("Pending");
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertThat(response.getBody()).contains("Order does not belong to user");
    }

    @Test
    void testConfirmOrder_NonExistentOrder_ThrowsNotFoundException() {
        String nonExistentOrderId = "nonExistentOrderId";

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                String.class,
                nonExistentOrderId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody()).contains("Order not found");
    }

    @Test
    @Disabled
    void testConfirmOrder_MissingToken_ThrowsUnauthorizedException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        order = orderRepository.save(order);

        HttpEntity<Void> request = new HttpEntity<>(null);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testConfirmOrder_InvalidToken_ThrowsUnauthorizedException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        orderRepository.save(order);

        HttpHeaders headers = getHttpHeadersWithToken("invalidToken");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}/confirm",
                HttpMethod.PATCH,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testEditOrder_PendingOrder_EditsOrder() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        order.setAddress("123 Main St");
        orderRepository.save(order);

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Order> request = new HttpEntity<>(updatedOrder, headers);

        ResponseEntity<Order> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                Order.class,
                order.getId()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("456 Elm St", response.getBody().getAddress());
    }

    @Test
    void testEditOrder_NonPendingOrder_ThrowsException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Confirmed");
        order.setAddress("123 Main St");
        orderRepository.save(order);

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Order> request = new HttpEntity<>(updatedOrder, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody()).contains("Order cannot be edited");
    }

    @Test
    void testEditOrder_OrderDoesNotBelongToUser_ThrowsUnauthorizedException() {
        User anotherUser = new User();
        anotherUser.setUserName("anotherUser");
        userRepository.save(anotherUser);

        Order order = new Order();
        order.setUserId(anotherUser.getId());
        order.setStatus("Pending");
        order.setAddress("123 Main St");
        orderRepository.save(order);

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Order> request = new HttpEntity<>(updatedOrder, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertThat(response.getBody()).contains("Order does not belong to user");
    }

    @Test
    void testEditOrder_NonExistentOrder_ThrowsNotFoundException() {
        String nonExistentOrderId = "nonExistentOrderId";

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpHeaders headers = getHttpHeadersWithToken(token);
        HttpEntity<Order> request = new HttpEntity<>(updatedOrder, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                String.class,
                nonExistentOrderId
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody()).contains("Order not found");
    }

    @Test
    @Disabled
    void testEditOrder_MissingToken_ThrowsUnauthorizedException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        order.setAddress("123 Main St");
        orderRepository.save(order);

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpEntity<Order> request = new HttpEntity<>(updatedOrder); // No token

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testEditOrder_InvalidToken_ThrowsUnauthorizedException() {
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("Pending");
        order.setAddress("123 Main St");
        orderRepository.save(order);

        Order updatedOrder = new Order();
        updatedOrder.setAddress("456 Elm St");

        HttpHeaders headers = getHttpHeadersWithToken("invalidToken");
        HttpEntity<Order> request = new HttpEntity<>(updatedOrder, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                request,
                String.class,
                order.getId()
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
