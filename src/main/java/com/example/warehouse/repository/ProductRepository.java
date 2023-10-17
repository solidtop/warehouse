package com.example.warehouse.repository;

import com.example.warehouse.dto.Metadata;
import com.example.warehouse.entity.Product;
import com.example.warehouse.dto.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    void save(@Valid Product product);
    List<Product> findAll();
    List<Product> findAll(@Valid Pagination pagination);
    List<Product> findByCategory(String category, @Valid Pagination pagination);
    Optional<Product> findById(String id);
    Metadata fetchMetadata(@Valid Pagination pagination);
}
