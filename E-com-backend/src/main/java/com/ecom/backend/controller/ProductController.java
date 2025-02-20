package com.ecom.backend.controller;

import com.ecom.backend.model.Product;
import com.ecom.backend.service.ProductService;
import org.slf4j.Logger;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    // FIXME: do i need to split category and subcategory into two endpoints

    // Fetch products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getProductByCategory(
            @PathVariable String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        if (page < 0 || limit <= 0) {
            return ResponseEntity.badRequest().body("Page must be >= 0 and limit > 0");
        }

        try {
            var products = productService.getProductsOfCategory(category, page, limit);


            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found for the given category.");
            }

            return ResponseEntity.ok(products.getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching products by category.");
        }
    }

    // Fetch products by category and subcategory
    @GetMapping("/categoryAndSubCategory/{category}/{subCategory}")
    public ResponseEntity<?> getProductsByCategoryAndSubCategory(
            @PathVariable String category,
            @PathVariable String subCategory,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        if (page < 0 || limit <= 0) {
            return ResponseEntity.badRequest().body("Page must be >= 0 and limit > 0");
        }

        try {
            var products = productService.getProductsOfCategoryAndSubCategory(category, subCategory, page, limit);

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No products found for the given category and subcategory.");
            }

            return ResponseEntity.ok(products.getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching products by category and subcategory.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> tag,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {


        List<Document> products = productService.searchProducts(name, category, subcategory, tag, minPrice, maxPrice, sortBy, sortOrder);
      
        return ResponseEntity.ok(products);
    }

    // TODO: make this sellers only
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Product product, UriComponentsBuilder ucb) {
        Product product1 =  productService.createProduct(product);
        URI uri = ucb.path("/api/products/{id}").buildAndExpand(product1.getId()).toUri();
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

    // for testing
    @GetMapping()
    public ResponseEntity<List<Product>> getProductById() {
        List<Product> product = productService.getAllProducts();
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
// TODO: limit the data return relevant to each request