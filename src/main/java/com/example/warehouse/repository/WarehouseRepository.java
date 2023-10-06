package com.example.warehouse.repository;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements ProductRepository {
    private final List<Product> products;

    public WarehouseRepository() {
        products = new ArrayList<>();
        products.add(new Product("0", "Product1", ProductCategory.BOOKS, 5, LocalDateTime.now(), LocalDateTime.now()));
        products.add(new Product("1", "Product2", ProductCategory.VIDEO_GAMES, 10, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Override
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
    public List<Product> findAll() {
        return Collections.unmodifiableList(products);
    }

    @Override
    public Optional<Product> findById(String productId) {
        return products.stream()
                .filter(product -> product.id().equals(productId))
                .findFirst();
    }


}
