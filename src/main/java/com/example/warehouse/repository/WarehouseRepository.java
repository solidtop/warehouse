package com.example.warehouse.repository;

import com.example.warehouse.dto.Metadata;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.Pagination;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public class WarehouseRepository implements ProductRepository {
    private final List<Product> products;

    public WarehouseRepository() {
        products = new ArrayList<>();
    }

    @Override
    @Lock(LockType.WRITE)
    public void save(@Valid Product product) {
        Optional<Product> productOptional = findById(product.id());
        if (productOptional.isPresent()) {
            int index = products.indexOf(productOptional.get());
            products.set(index, product);
            return;
        }

        products.add(product);
    }

    @Override
    @Lock(LockType.READ)
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    @Override
    @Lock(LockType.READ)
    public List<Product> findAll(@Valid Pagination pagination) {
        long page = pagination.getPage();
        long limit = pagination.getLimit();

        long offset = (page - 1) * limit;

        return products.stream()
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    @Lock(LockType.READ)
    public Optional<Product> findById(@NotNull String productId) {
        return products.stream()
                .filter(product -> product.id().equals(productId))
                .findFirst();
    }

    @Override
    @Lock(LockType.READ)
    public Metadata fetchMetadata(@Valid Pagination pagination) {
        long totalPages = Math.round((float) products.size() / pagination.getLimit());
        return new Metadata(pagination.getPage(), pagination.getLimit(), products.size(), totalPages);
    }
}
