package com.example.warehouse.repository;

import com.example.warehouse.entity.Product;
import com.example.warehouse.dto.Pagination;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    List<Product> findAll();
    List<Product> findAll(Pagination pagination);
    Optional<Product> findById(String id);
}
