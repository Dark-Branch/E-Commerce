package com.ecom.backend.repositoty;

import com.ecom.backend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);
}
