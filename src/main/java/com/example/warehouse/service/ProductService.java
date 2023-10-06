package com.example.warehouse.service;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product addNewProduct(String name, ProductCategory category, int rating);
    Product updateProduct(String productId, String name);
    Product updateProduct(String productId, ProductCategory category);
    Product updateProduct(String productId, int rating);
    Product updateProduct(String productId, String name, ProductCategory category, int rating);
    List<Product> getAllProducts();
    Optional<Product> getProductById(String productId);
}
