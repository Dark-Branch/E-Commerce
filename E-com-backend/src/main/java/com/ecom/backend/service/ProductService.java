package com.ecom.backend.service;

import com.ecom.backend.model.Product;
import com.ecom.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

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
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
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
}

// TODO: do we use enum for category
// add paging with sorting

