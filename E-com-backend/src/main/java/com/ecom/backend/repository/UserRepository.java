package com.ecom.backend.repository;

import com.ecom.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Optional<User> findByUserName(String username);
    void deleteByUserName(String userName);
}
