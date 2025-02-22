package com.ecom.backend.repository;

import com.ecom.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>, PagingAndSortingRepository<Product, String> {
    Page<Product> findByCategory(String category, PageRequest pageRequest);
    Page<Product> findByCategoryAndSubCategory(String category, String subCategory, PageRequest pageRequest);
}
