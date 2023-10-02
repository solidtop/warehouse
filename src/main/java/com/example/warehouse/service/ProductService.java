package com.example.warehouse.service;

import com.example.warehouse.entities.Product;
import com.example.warehouse.entities.ProductCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    Product addNewProduct(String name, ProductCategory category, int rating);
    Product updateProduct(String productId, String name, ProductCategory category, int rating);
    List<Product> getAllProducts();
    Optional<Product> getProductById(String productId);
    List<Product> getProductsByCategory(ProductCategory category);
    List<Product> getProductsSince(LocalDate date);
    List<Product> getModifiedProducts();
    Set<ProductCategory> getCategoriesWithProducts();
    int getProductCountInCategory(ProductCategory category);
    List<Product> getTopRatedProductsThisMonth();
    Map<Character, Long> getProductCountByFirstLetter();
}
