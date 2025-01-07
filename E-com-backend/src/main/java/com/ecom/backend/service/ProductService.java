package com.ecom.backend.service;

import com.ecom.backend.exception.ProductNotFoundException;
import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

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

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(String productId) {
        return productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
    }

    public Product updateProduct(String productId, Product updatedProduct) throws Exception{
        // TODO: this entry sucks, can cause data loss
        // doing this we should consider when retrieving we should output complete object else all data will be lost
        Product existingProduct = getProductById(productId);
        updatedProduct.setId(existingProduct.getId());
        return productRepository.save(updatedProduct);
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

