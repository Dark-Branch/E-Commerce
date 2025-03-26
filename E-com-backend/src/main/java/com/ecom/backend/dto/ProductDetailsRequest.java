package com.ecom.backend.dto;

import com.ecom.backend.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDetailsRequest {
    private String id;
    private String name;
    private double price;
    private double rating; // (0-5)
    private String brand;
    private String category;
    private String subCategory;
    private List<String> tags;
    private int inventoryCount; // Number of items left
    private List<String> otherImages;
    private String description;
    private List<Product.Spec> specs;

}


