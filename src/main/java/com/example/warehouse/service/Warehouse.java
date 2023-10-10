package com.example.warehouse.service;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.entity.Products;
import com.example.warehouse.dto.Pagination;
import com.example.warehouse.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class Warehouse implements ProductService {
    private ProductRepository productRepository;
    private Clock clock;

    public Warehouse() {}

    @Inject
    public Warehouse(ProductRepository productRepository) {
        this.productRepository = productRepository;
        clock = Clock.systemDefaultZone();
    }

    public Warehouse(ProductRepository productRepository, Clock clock) {
        this.productRepository = productRepository;
        this.clock = clock;
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
    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProducts(Pagination pagination) {
        return productRepository.findAll(pagination);
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.category() == category)
                .sorted(Comparator.comparing(Product::name))
                .toList();
    }

    @Override
    public List<Product> getProductsSince(LocalDate date) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.createdAt().isAfter(date.atStartOfDay()))
                .toList();
    }

    @Override
    public List<Product> getModifiedProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.updatedAt().isAfter(product.createdAt()))
                .toList();
    }

    @Override
    public List<Product> getTopRatedProductsThisMonth() {
        LocalDate today = LocalDate.now(clock);
        int currentMonth = today.getMonthValue();

        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.createdAt().getMonthValue() == currentMonth)
                .filter(product -> product.rating() == Products.MAX_RATING)
                .sorted(Comparator.comparing(Product::createdAt).reversed())
                .toList();
    }

    @Override
    public Set<ProductCategory> getCategoriesWithProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(Product::category)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Map<Character, Long> getProductCountByFirstLetter() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .collect(Collectors.groupingBy(
                        product -> product.name().charAt(0),
                        Collectors.counting()
                ));
    }

    @Override
    public int getProductCountInCategory(ProductCategory category) {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.category() == category)
                .mapToInt(product -> 1)
                .reduce(0, Integer::sum);
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
