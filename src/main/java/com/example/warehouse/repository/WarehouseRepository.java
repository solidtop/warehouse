package com.example.warehouse.repository;

import com.example.warehouse.entity.Product;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;

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
    public Optional<Product> findById(String productId) {
        return products.stream()
                .filter(product -> product.id().equals(productId))
                .findFirst();
    }
}
