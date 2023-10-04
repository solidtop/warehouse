package com.example.warehouse.service;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public interface ProductService {
    @Lock(LockType.WRITE)
    Product addNewProduct(String name, ProductCategory category, int rating);
    @Lock(LockType.WRITE)
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
