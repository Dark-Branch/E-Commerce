package com.ecom.backend.repositoty;

import com.ecom.backend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // Add custom queries if needed
}
