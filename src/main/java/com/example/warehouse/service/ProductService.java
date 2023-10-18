package com.example.warehouse.service;

import com.example.warehouse.dto.*;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.Pagination;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    Product addNewProduct(@Valid ProductDTO productDTO);
    Product updateProduct(String productId, @Valid NameDTO nameDTO);
    Product updateProduct(String productId, @Valid CategoryDTO categoryDTO);
    Product updateProduct(String productId, @Valid RatingDTO ratingDTO);
    Product updateProduct(String productId, @Valid ProductDTO productDTO);
    Product getProductById(String productId);
    List<Product> getAllProducts(@Valid Pagination pagination);
    List<Product> getProductsByCategory(String category, @Valid Pagination pagination);
    List<Product> getProductsSince(LocalDate date);
    List<Product> getModifiedProducts();
    List<Product> getTopRatedProductsThisMonth();
    Set<ProductCategory> getCategoriesWithProducts();
    Map<Character, Long> getProductCountByFirstLetter();
    int getProductCountInCategory(String category);
    Metadata getMetadata(@Valid Pagination pagination);
}
