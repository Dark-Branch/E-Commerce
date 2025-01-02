package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // change this to give from each category
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // sellers only
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Product product, UriComponentsBuilder ucb) {

        Product product1 =  productService.createProduct(product);
        URI uri = ucb.path("/products/{id}").buildAndExpand(product1.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    // give all details of the product
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // only sellers
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // only sellers
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
}
