package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.exception.UnauthorizedException;
import com.ecom.backend.model.Order;
import com.ecom.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order getOrderById(String id, String userId)  {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order Not Found"));
        if (!Objects.equals(order.getUserName(), userId)){
            throw new UnauthorizedException("Order does not belong to user");
        }
        return order;
    }

    public List<Order> getOrdersByUserName(String userName) {
        return orderRepository.findByUserName(userName);
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}
