package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // change this to give from each category
    @GetMapping("/category/{category}")
    // unknown type because error is also can be returned
    public ResponseEntity<?> getProductByCategory(
            @PathVariable String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "subCategory", required = false) String subCategory) {

        if (page < 0 || limit <= 0) {
            return ResponseEntity.badRequest().body("Page must be >= 0 and limit > 0");
        }

        if (subCategory == null) {
            var products = productService.getProductsOfCategory(category, page, limit);
            return ResponseEntity.ok(products.getContent());
        }

        var products = productService.getProductsOfCategoryAndSubCategory(category, subCategory, page, limit);
        return ResponseEntity.ok(products.getContent());
    }
    // FIXME: do i need to split category and subcategory into two endpoints

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
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product updatedProduct;
        try {
            updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException runtimeException){
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            logger.error("Internal server error: ", e); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // only sellers
    // TODO: implement properly
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    }

// TODO: update product things
