package com.ecom.backend.controller;

import com.ecom.backend.model.Order;
import com.ecom.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order, UriComponentsBuilder ucb) {
        Order newOrder =  orderService.createOrder(order);
        URI uri = ucb.path("/products/{id}").buildAndExpand(newOrder.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
