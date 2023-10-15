package com.example.warehouse.service;

import com.example.warehouse.dto.Metadata;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.Pagination;
import com.example.warehouse.exception.ProductNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    Product addNewProduct(String name, ProductCategory category, int rating);
    Product updateProduct(String productId, String name);
    Product updateProduct(String productId, ProductCategory category);
    Product updateProduct(String productId, int rating);
    Product updateProduct(String productId, String name, ProductCategory category, int rating);
    Product getProductById(String productId);
    List<Product> getAllProducts();
    List<Product> getAllProducts(@Valid Pagination pagination);
    List<Product> getProductsByCategory(ProductCategory category);
    List<Product> getProductsSince(LocalDate date);
    List<Product> getModifiedProducts();
    List<Product> getTopRatedProductsThisMonth();
    Set<ProductCategory> getCategoriesWithProducts();
    Map<Character, Long> getProductCountByFirstLetter();
    int getProductCountInCategory(ProductCategory category);
    Metadata getMetadata(@Valid Pagination pagination);
}
