package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.model.Order;
import com.ecom.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(String id){
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order Not Found"));
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}
