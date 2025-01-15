package com.ecom.backend.repository;

import com.ecom.backend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserName(String userName);
    Optional<Cart> findByUserId(String userId);
    Optional<Cart> findBySessionId(String sessionId);
}
// TODO: do the refractors related to change of userid to user name