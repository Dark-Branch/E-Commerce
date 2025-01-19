package com.ecom.backend.service;

import com.ecom.backend.exception.ProductNotFoundException;
import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public ProductService(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProductsOfCategory(String category, int page, int limit) {
        return productRepository.findByCategory(category,
                PageRequest.of(page, limit));
    }

    public Page<Product> getProductsOfCategoryAndSubCategory(String category, String subCategory, int page, int limit) {
        return productRepository.findByCategoryAndSubCategory(category, subCategory,
                PageRequest.of(page, limit));
    }

    public List<Document> searchProducts(String name, String category, String subCategory, List<String> tags, Double minPrice, Double maxPrice, String sortBy, String sortOrder) {

        Query query = new Query(); //A new Query object is created. This object is used to build the query for fetching data from the MongoDB database.

        // FIXME: for now, only use regex, change this
        // TODO: use tags, categories also for search
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(name, "i")); // The regex(name, "i") method is used, which performs a case-insensitive search for the product name. The "i" flag ensures the search ignores letter casing (e.g., "phone" will match "Phone", "PHONE", etc.).
        }
        if (category != null) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (subCategory != null) {
            query.addCriteria(Criteria.where("category").is(subCategory));
        }
        // Check if the 'tags' parameter is provided
        if (tags != null && !tags.isEmpty()) {
            // Search products where any of the provided tags match
            query.addCriteria(Criteria.where("tags").in(tags)); // The "in" method finds products whose tags contain any of the provided tags.
        }
        // specially wrote this query because prev method didnt work when two criteria for one field not worked
        if (minPrice != null || maxPrice != null) {
            Document priceQuery = new Document();

            if (minPrice != null) {
                priceQuery.put("$gte", minPrice);
            }
            if (maxPrice != null) {
                priceQuery.put("$lte", maxPrice);
            }

            query.addCriteria(Criteria.where("price").is(priceQuery));
        }

        // sort
        if ("price".equalsIgnoreCase(sortBy)) {
            query.with(org.springframework.data.domain.Sort.by( //Sort.by(): It accepts a direction (Sort.Direction.ASC or Sort.Direction.DESC) and the field name (either "price" or "name") to sort by.
                    "asc".equalsIgnoreCase(sortOrder) ?
                            org.springframework.data.domain.Sort.Direction.ASC :
                            org.springframework.data.domain.Sort.Direction.DESC,
                    "price"
            ));
        } else if ("name".equalsIgnoreCase(sortBy)) {
            query.with(org.springframework.data.domain.Sort.by(
                    "asc".equalsIgnoreCase(sortOrder) ?
                            org.springframework.data.domain.Sort.Direction.ASC :
                            org.springframework.data.domain.Sort.Direction.DESC,
                    "name"
            ));
        }

        return mongoTemplate.find(query, Document.class, "products"); //The constructed query is executed using mongoTemplate.find(), which fetches the documents (products) that match the query criteria from the products collection in MongoDB.
    }

//    Summary of How This Method Works:
//    Filters: The method dynamically adds filtering conditions to the query based on the provided parameters (e.g., name, category, minPrice, maxPrice).
//    Sorting: It applies sorting based on either price or name and the sorting direction (asc or desc).
//    Query Execution: The mongoTemplate executes the query and returns the matching products.


    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(String productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Product updateProduct(String productId, Product updatedProduct) throws Exception {
        // TODO: this entry sucks, can cause data loss
        // doing this we should consider when retrieving we should

        // Retrieve the existing product or throw an exception if it doesn't exist
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));


        if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) {
            existingProduct.setName(updatedProduct.getName());
        } else if (updatedProduct.getName() != null) {
            throw new IllegalArgumentException("Product name cannot be blank.");
        }

        if (updatedProduct.getPrice() >= 0) { // Ensure price is valid (non-negative)
            existingProduct.setPrice(updatedProduct.getPrice());
        } else if (updatedProduct.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }

        if (updatedProduct.getCategory() != null) {
            existingProduct.setCategory(updatedProduct.getCategory());
        }

        if (updatedProduct.getSubCategory() != null) {
            existingProduct.setSubCategory(updatedProduct.getSubCategory());
        }

        if (updatedProduct.getInventoryCount() >= 0) {
            existingProduct.setInventoryCount(updatedProduct.getInventoryCount());
        }

        if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isBlank()) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }

        if (updatedProduct.getBrand() != null) {
            existingProduct.setBrand(updatedProduct.getBrand());
        }

        if (updatedProduct.getMainImg() != null) {
            existingProduct.setMainImg(updatedProduct.getMainImg());
        }

        if (updatedProduct.getTags() != null && !updatedProduct.getTags().isEmpty()) {
            existingProduct.setTags(updatedProduct.getTags());
        }

        if (updatedProduct.getOtherImages() != null && !updatedProduct.getOtherImages().isEmpty()) {
            existingProduct.setOtherImages(updatedProduct.getOtherImages());
        }

        if (updatedProduct.getRating() >= 0 && updatedProduct.getRating() <= 5) { // Ensure rating is within range
            existingProduct.setRating(updatedProduct.getRating());
        } else if (updatedProduct.getRating() < 0 || updatedProduct.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }

        // Set the ID to ensure consistency and save the updated product
        updatedProduct.setId(existingProduct.getId());
        return productRepository.save(existingProduct); // Save the modified `existingProduct` instead of `updatedProduct`
    }


    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }


    public boolean isProductInStock(String productId, int requestedQuantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                ProductNotFoundException::new
        );
        return product.getInventoryCount() >= requestedQuantity;
    }


    public boolean reduceStock(String productId, int quantity) {
        Query query = new Query(Criteria.where("_id").is(productId).and("inventoryCount").gte(quantity));
        Update update = new Update().inc("inventoryCount", -quantity);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        return result.getModifiedCount() > 0;
    }

    public double getPriceByProductId(String productId){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()){
            return product.get().getPrice();
        }
        throw new ProductNotFoundException();
    }
}


// TODO: do we use enum for category
// add paging with sorting

