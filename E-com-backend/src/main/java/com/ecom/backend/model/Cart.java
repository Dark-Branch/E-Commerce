package com.ecom.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userName;    // TODO: safely remove this
    private String userId;  // for registered users
    private String sessionId;   // for guests
    private List<CartItem> items;  // when creating a cart, make this for sure
    private boolean active;
    private Date createdAt;
    private Date updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String productId;
        private int quantity;
        private String version;
        private Double price;
    }
}
// TODO: add discounts
