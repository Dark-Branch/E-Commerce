package com.ecom.backend.controller;

import com.ecom.backend.model.Order;
import com.ecom.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/history")
    public ResponseEntity<List<Order>> getOrderHistory(Principal principal) {
        String userId = principal.getName();
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id, Principal principal) {
        String userId = principal.getName();
        Order order = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order,
                                            UriComponentsBuilder ucb,
                                            Principal principal) {
        String username = principal.getName();
        order.setUserId(username);
        Order newOrder = orderService.createOrder(order);
        URI uri = ucb.path("/api/orders/{id}").buildAndExpand(newOrder.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
