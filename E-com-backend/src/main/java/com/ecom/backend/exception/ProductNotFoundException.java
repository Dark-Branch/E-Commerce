package com.ecom.backend.exception;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(){
        super("Product not found");
    }
}
