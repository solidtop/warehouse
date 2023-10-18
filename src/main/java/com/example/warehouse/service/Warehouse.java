package com.example.warehouse.service;

import com.example.warehouse.dto.*;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.entity.Products;
import com.example.warehouse.exception.ProductNotFoundException;
import com.example.warehouse.dto.Pagination;
import com.example.warehouse.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

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
    public Product addNewProduct(@Valid ProductDTO productDTO) {
        String id = generateId();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        ProductCategory category = ProductCategory.toCategory(productDTO.category());
        Product product = new Product(id, productDTO.name(), category, productDTO.rating(), createdAt, updatedAt);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(String productId, @Valid ProductDTO productDTO) {
        Product product = getProductById(productId);
        return updateProduct(product, productDTO);
    }

    @Override
    public Product updateProduct(String productId, @Valid NameDTO nameDTO) {
        Product product = getProductById(productId);
        ProductDTO productDTO = new ProductDTO(nameDTO.name(), product.category().toString(), product.rating());
        return updateProduct(product, productDTO);
    }

    @Override
    public Product updateProduct(String productId, @Valid CategoryDTO categoryDTO) {
        Product product = getProductById(productId);
        ProductDTO productDTO = new ProductDTO(product.name(), categoryDTO.category(), product.rating());
        return updateProduct(product, productDTO);
    }

    @Override
    public Product updateProduct(String productId, @Valid RatingDTO ratingDTO) {
        Product product = getProductById(productId);
        ProductDTO productDTO = new ProductDTO(product.name(), product.category().toString(), ratingDTO.rating());
        return updateProduct(product, productDTO);
    }

    private Product updateProduct(@Valid Product product, @Valid ProductDTO productDto) {
        String name = productDto.name();
        ProductCategory category = ProductCategory.toCategory(productDto.category());
        int rating = productDto.rating();
        LocalDateTime updatedAt = LocalDateTime.now(clock);
        Product updatedProduct = new Product(product.id(), name, category, rating, product.createdAt(), updatedAt);
        productRepository.save(updatedProduct);
        return updatedProduct;
    }

    @Override
    public Product getProductById(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> getAllProducts(@Valid Pagination pagination) {
        return productRepository.findAll(pagination);
    }

    @Override
    public List<Product> getProductsByCategory(String category, @Valid Pagination pagination) {
        return productRepository.findByCategory(category, pagination);
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
    public int getProductCountInCategory(String category) {
        ProductCategory productCategory = ProductCategory.toCategory(category);
        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.category() == productCategory)
                .mapToInt(product -> 1)
                .reduce(0, Integer::sum);
    }

    @Override
    public Metadata getMetadata(@Valid Pagination pagination) {
        return productRepository.fetchMetadata(pagination);
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
