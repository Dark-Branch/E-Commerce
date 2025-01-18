package com.ecom.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    private String name;
    private double price;
    private double rating; // (0-5)
    private String mainImg;
    private String brand;
    private String category;
    private String subCategory;
    private List<String> tags;
    private int inventoryCount; // Number of items left
    private List<String> otherImages;
    private String description;
    private String sellerId;

    private List<Spec> specs; // Specifications of the product

    public Product(String name, String category, String subCategory, double price , int inventoryCount , String sellerId) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.inventoryCount = inventoryCount;
        this.sellerId = sellerId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Spec {
        private String spec; // Specification name
        private String value; // Value of the specification
    }
}
