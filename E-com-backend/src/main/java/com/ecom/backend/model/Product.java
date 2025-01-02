package com.ecom.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id; // Unique identifier for the product
    private String name; // Product name
    private double price; // Product price
    private double rating; // Product rating (0-5)
    private String mainImg; // URL for the main product image
    private String brand; // Brand of the product
    private String category; // Primary category of the product
    private String subCategory; // Sub-category of the product
    private List<String> tags; // Tags associated with the product (max 10)
    private int inventoryCount; // Number of items left in inventory
    private List<String> otherImages; // URLs of additional images
    private String description; // Detailed product description

    private List<Spec> specs; // Specifications of the product

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Spec {
        private String spec; // Specification name
        private String value; // Value of the specification
    }
}
