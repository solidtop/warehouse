package com.example.warehouse.repository;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.Pagination;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

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
        for(int i = 0; i < 50; i++) {
            products.add(new Product(Integer.toString(i), "Product" + i, ProductCategory.MUSIC, (int) Math.round(Math.random() * 10), LocalDateTime.now(), LocalDateTime.now()));
        }
    }

    @Override
    @Lock(LockType.WRITE)
    public void save(Product product) {
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
    public List<Product> findAll(Pagination pagination) {
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
    public Optional<Product> findById(String productId) {
        return products.stream()
                .filter(product -> product.id().equals(productId))
                .findFirst();
    }
}
