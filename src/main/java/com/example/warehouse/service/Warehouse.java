package com.example.warehouse.service;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.entity.Products;
import com.example.warehouse.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@ApplicationScoped
public class Warehouse implements ProductService {
    private ProductRepository productRepository;
    private Clock clock;

    public Warehouse() {}

    public Warehouse(Clock clock) {
        this.clock = clock;
    }

    @Inject
    public Warehouse(ProductRepository productRepository) {
        this.productRepository = productRepository;
        clock = Clock.systemDefaultZone();
    }

    @Override
    public Product addNewProduct(String name, ProductCategory category, int rating) {
        validateName(name);
        validateRating(rating);

        String id = generateId();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        Product product = new Product(id, name, category, rating, createdAt, updatedAt);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(String productId, String name, ProductCategory category, int rating) {
        Product product = getProductByIdThrows(productId);
        validateName(name);
        validateRating(rating);
        return updateProduct(product, name, category, rating);
    }

    @Override
    public Product updateProduct(String productId, String name) {
        Product product = getProductByIdThrows(productId);
        validateName(name);
        return updateProduct(product, name, product.category(), product.rating());
    }

    @Override
    public Product updateProduct(String productId, ProductCategory category) {
        Product product = getProductByIdThrows(productId);
        return updateProduct(product, product.name(), category, product.rating());
    }

    @Override
    public Product updateProduct(String productId, int rating) {
        Product product = getProductByIdThrows(productId);
        validateRating(rating);
        return updateProduct(product, product.name(), product.category(), rating);
    }

    private Product updateProduct(Product product, String name, ProductCategory category, int rating) {
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        Product updatedProduct = new Product(product.id(), name, category, rating, product.createdAt(), updatedAt);
        productRepository.save(updatedProduct);
        return updatedProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void validateName(String name) throws IllegalArgumentException {
        if (name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");
    }

    private void validateRating(int rating) throws IllegalArgumentException {
        if (rating < Products.MIN_RATING || rating > Products.MAX_RATING)
            throw new IllegalArgumentException("Rating must be between " + Products.MIN_RATING + " and " + Products.MAX_RATING);
    }

    private Product getProductByIdThrows(String productId) throws NoSuchElementException {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(() -> new NoSuchElementException("Product not found"));
    }
}
