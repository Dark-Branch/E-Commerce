package com.ecom.backend.service;

import com.ecom.backend.exception.NotFoundException;
import com.ecom.backend.exception.UnauthorizedException;
import com.ecom.backend.model.Order;
import com.ecom.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private static final Set<String> ALLOWED_STATUSES = Set.of("Pending", "Confirmed", "Cancelled");

    public Order getOrderById(String id, String userId)  {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
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

    @Transactional
    public void confirmOrder(String id, String userId) {
        Order order = getOrderById(id, userId);
        if (!"Pending".equals(order.getStatus())) {
            throw new IllegalStateException("Order cannot be confirmed. Current status: " + order.getStatus());
        }
        order.setStatus("Confirmed");
        orderRepository.save(order);
    }

    @Transactional
    public Order editOrder(String id, Order updatedOrder, String userId) {
        Order existingOrder = getOrderById(id, userId);
        if (!"Pending".equals(existingOrder.getStatus())) {
            throw new IllegalStateException("Order cannot be edited. Current status: " + existingOrder.getStatus());
        }

        if (updatedOrder.getAddress() != null) {
            existingOrder.setAddress(updatedOrder.getAddress());
        }
        if (updatedOrder.getInstructions() != null) {
            existingOrder.setInstructions(updatedOrder.getInstructions());
        }
        if (updatedOrder.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        }
        if (updatedOrder.getItems() != null) {
            existingOrder.setItems(updatedOrder.getItems());
            existingOrder.setTotalAmount(calculateTotalAmount(updatedOrder.getItems()));
        }

        return orderRepository.save(existingOrder);
    }

    private double calculateTotalAmount(List<Order.OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
