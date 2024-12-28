package com.ecom.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId; // Reference to User
    private List<OrderItem> items;
    private double totalAmount;
    private String paymentMethod; // E.g., "Credit Card", "PayPal"
    private String status; // E.g., "Pending", "Shipped", "Delivered"
    private Date createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String productId; // Reference to Product
        private int quantity;
        private double price; // Price at the time of order
    }
}
