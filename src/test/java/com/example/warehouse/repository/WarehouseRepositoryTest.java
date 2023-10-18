package com.example.warehouse.repository;

import com.example.warehouse.dto.Pagination;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseRepositoryTest {
    WarehouseRepository warehouseRepository;
    Product mockProduct;
    List<Product> mockProducts;
    LocalDateTime now;
    Pagination mockPagination;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        mockProduct = new Product("1", "Product1", ProductCategory.BOOKS, 0, now, now);
        mockProducts = Arrays.asList(
                mockProduct,
                new Product("2", "Product2", ProductCategory.BOOKS, 0, now, now)
        );
        warehouseRepository = new WarehouseRepository(mockProducts);
        mockPagination = new Pagination().setPage(1).setLimit(10);
    }

    @Test
    void Should_SaveNewProduct() {
        Product newProduct = new Product("3", "NewProduct", ProductCategory.BOOKS, 0, now, now);

        warehouseRepository.save(newProduct);

        assertTrue(mockProducts.contains(newProduct));
    }

    @Test
    void Should_UpdateExistingProduct() {
        Product updatedProduct = new Product("1", "UpdatedProduct", ProductCategory.MUSIC, 0, now, now);

        warehouseRepository.save(updatedProduct);

        assertTrue(mockProducts.contains(updatedProduct));
        assertFalse(mockProducts.contains(mockProduct));
    }
    
    @Test
    void Should_ReturnAllProducts() {
        List<Product> products = warehouseRepository.findAll(mockPagination);

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnPaginatedProducts() {
        Pagination pagination = new Pagination().setPage(1).setLimit(1);

        List<Product> products = warehouseRepository.findAll(pagination);

        int expected = 1;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnProduct_IfExists() {
        Optional<Product> productOptional = warehouseRepository.findById(mockProduct.id());

        assertTrue(productOptional.isPresent());
    }

    @Test
    void Should_ReturnEmptyOptional_IfProductNotFound() {
        Optional<Product> productOptional = warehouseRepository.findById("-1");

        assertTrue(productOptional.isEmpty());
    }

    @Test
    void Should_ReturnProductsInCategorySortedByAlphabeticalOrder() {
        WarehouseRepository warehouseRepository = new WarehouseRepository(List.of(
                new Product("1", "C", ProductCategory.BOOKS, 5, now, now),
                new Product("2", "B", ProductCategory.MUSIC, 5, now, now),
                new Product("3", "A", ProductCategory.BOOKS, 5, now, now)
        ));

        List<Product> booksProducts = warehouseRepository.findByCategory("books", mockPagination);

        int expected = 2;
        int actual = booksProducts.size();
        assertEquals(expected, actual);
        assertEquals('A', booksProducts.get(0).name().charAt(0));
    }

    @Test
    void Should_ReturnPaginatedProductsInCategory() {
        WarehouseRepository warehouseRepository = new WarehouseRepository(List.of(
                new Product("1", "C", ProductCategory.BOOKS, 5, now, now),
                new Product("2", "B", ProductCategory.MUSIC, 5, now, now),
                new Product("3", "A", ProductCategory.BOOKS, 5, now, now)
        ));
        Pagination pagination = new Pagination().setPage(1).setLimit(1);

        List<Product> products = warehouseRepository.findByCategory("books", pagination);

        int expected = 1;
        int actual = products.size();
        assertEquals(expected, actual);
    }
}