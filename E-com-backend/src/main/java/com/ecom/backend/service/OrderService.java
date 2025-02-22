package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.exception.UnauthorizedException;
import com.ecom.backend.model.Order;
import com.ecom.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(String id, String userId)  {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order Not Found"));
        if (!Objects.equals(order.getUserId(), userId)){
            throw new UnauthorizedException("Order does not belong to user");
        }
        return order;
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserIdAndStatusNot(userId, "Cancelled");
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}
