package com.example.warehouse.service;

import com.example.warehouse.dto.*;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.exception.ProductNotFoundException;
import com.example.warehouse.dto.Pagination;
import com.example.warehouse.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WarehouseTest {
    @Mock
    private ProductRepository productRepository;
    private Warehouse warehouse;
    private LocalDateTime now;
    private Product mockProduct;
    private Pagination mockPagination;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        warehouse = new Warehouse(productRepository, fixedClock);

        now = LocalDateTime.now(fixedClock);
        mockProduct = new Product("1", "Product1", ProductCategory.BOOKS, 0, now, now);
        List<Product> mockProducts = Arrays.asList(
                mockProduct,
                new Product("2", "Product2", ProductCategory.BOOKS, 0, now, now)
        );
        mockPagination = new Pagination().setPage(1).setLimit(10);

        when(productRepository.findAll()).thenReturn(mockProducts);
        when(productRepository.findAll(mockPagination)).thenReturn(mockProducts);
        when(productRepository.findById(mockProduct.id())).thenReturn(Optional.of(mockProduct));
        doNothing().when(productRepository).save(any());
    }

    @Test
    void Should_AddNewProduct() {
        ProductDTO productDTO = new ProductDTO("TestProduct", "books", 5);
        Product product = warehouse.addNewProduct(productDTO);

        assertNotNull(product);
        assertEquals("TestProduct", product.name());
        assertEquals(ProductCategory.BOOKS, product.category());
        assertEquals(5, product.rating());
        assertNotNull(product.id());
        assertEquals(now, product.createdAt());
        assertEquals(now, product.updatedAt());
    }

    @Test
    void Should_ReturnAllProducts() {
        List<Product> products = warehouse.getAllProducts(mockPagination);

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnProduct_IfExists() {
        Product product = warehouse.getProductById(mockProduct.id());

        assertNotNull(product);
        assertEquals(mockProduct, product);
    }

    @Test
    void Should_ThrowException_IfProductNotExists() {
        when(productRepository.findById(mockProduct.id())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            warehouse.getProductById(mockProduct.id());
        });
    }

    @Test
    void Should_UpdateProductName() {
        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), new NameDTO("UpdatedProduct"));

        String expected = "UpdatedProduct";
        String actual = updatedProduct.name();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateProductCategory() {
        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), new CategoryDTO("music"));

        ProductCategory expected = ProductCategory.MUSIC;
        ProductCategory actual = updatedProduct.category();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateProductRating() {
        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), new RatingDTO(5));

        int expected = 5;
        int actual = updatedProduct.rating();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateAllProductDetails() {
        LocalDateTime createdAt = now.minusMinutes(1);
        Product mockProduct = new Product("1", "Product", ProductCategory.BOOKS, 2, createdAt, createdAt);
        ProductDTO productDto = new ProductDTO("UpdatedProduct", "video_games", 10);

        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), productDto);

        assertEquals("UpdatedProduct", updatedProduct.name());
        assertEquals(ProductCategory.VIDEO_GAMES, updatedProduct.category());
        assertEquals(10, updatedProduct.rating());
        assertTrue(updatedProduct.updatedAt().isAfter(createdAt));
    }

    @Test
    void Should_ReturnProductsInCategory() {
        when(productRepository.findByCategory("books", mockPagination)).thenReturn(List.of(
                new Product("1", "A", ProductCategory.BOOKS, 5, now, now),
                new Product("2", "B", ProductCategory.BOOKS, 5, now, now)
        ));

        List<Product> booksProducts = warehouse.getProductsByCategory("books", mockPagination);

        int expected = 2;
        int actual = booksProducts.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnProductsSinceSpecifiedDate() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 5, now.minusDays(5), now.minusDays(5)),
                new Product("2", "Product2", ProductCategory.MUSIC, 6, now.minusDays(3), now.minusDays(3)),
                new Product("3", "Product3", ProductCategory.BOOKS, 8, now, now)
        ));

        LocalDate specifiedDate = now.toLocalDate().minusDays(3);
        List<Product> products = warehouse.getProductsSince(specifiedDate);

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnModifiedProducts() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 8, now, now.plusMinutes(1)),
                new Product("2", "Product2", ProductCategory.BOOKS, 8, now, now),
                new Product("3", "Product3", ProductCategory.BOOKS, 8, now, now.plusHours(1))
        ));

        List<Product> products = warehouse.getModifiedProducts();

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnCategoriesTiedToAtLeast1Product() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 8, now, now),
                new Product("2", "Product2", ProductCategory.VIDEO_GAMES, 4, now, now),
                new Product("3", "Product3", ProductCategory.BOOKS, 3, now, now)
        ));

        Set<ProductCategory> categories = warehouse.getCategoriesWithProducts();

        int expected = 2;
        int actual = categories.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnProductCountInSpecifiedCategory() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 8, now, now),
                new Product("2", "Product2", ProductCategory.VIDEO_GAMES, 4, now, now),
                new Product("3", "Product3", ProductCategory.BOOKS, 3, now, now)
        ));

        long productCount = warehouse.getProductCountInCategory("books");

        int expected = 2;
        assertEquals(expected, productCount);
    }

    @Test
    void Should_ReturnProductsWithMaxRatingThisMonthSortedByNewest() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 10, now.minusMonths(1), now.minusMonths(1)),
                new Product("2", "Product2", ProductCategory.BOOKS, 10, now.minusMinutes(2), now.minusMinutes(2)),
                new Product("3", "Product3", ProductCategory.BOOKS, 10, now.minusMinutes(1), now.minusMinutes(1)),
                new Product("4", "Product4", ProductCategory.BOOKS, 9, now, now)
        ));

        List<Product> products = warehouse.getTopRatedProductsThisMonth();

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
        boolean condition = products.get(0).createdAt().isAfter(products.get(1).createdAt());
        assertTrue(condition);
    }

    @Test
    void Should_ReturnProductCountByFirstLetter() {
        when(productRepository.findAll()).thenReturn(List.of(
                new Product("1", "Abc", ProductCategory.BOOKS, 10, now, now),
                new Product("2", "Bcd", ProductCategory.BOOKS, 10, now, now),
                new Product("3", "Abc", ProductCategory.BOOKS, 10, now, now)
        ));

        Map<Character, Long> productMap = warehouse.getProductCountByFirstLetter();

        int expected = 2;
        int actual = productMap.size();
        assertEquals(expected, actual);
        assertEquals(2L, productMap.get('A'));
    }

    @Test
    void Should_ReturnEmptyResult_When_NoProductsFound() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        Pagination pagination = new Pagination();
        List<Product> allProducts = warehouse.getAllProducts(pagination);
        List<Product> booksProducts = warehouse.getProductsByCategory("books", pagination);
        List<Product> productsSince = warehouse.getProductsSince(now.toLocalDate());
        List<Product> modifiedProducts = warehouse.getModifiedProducts();
        Set<ProductCategory> categories = warehouse.getCategoriesWithProducts();
        List<Product> topRatedProducts = warehouse.getTopRatedProductsThisMonth();
        Map<Character, Long> productMap = warehouse.getProductCountByFirstLetter();

        assertTrue(allProducts.isEmpty());
        assertTrue(booksProducts.isEmpty());
        assertTrue(productsSince.isEmpty());
        assertTrue(modifiedProducts.isEmpty());
        assertTrue(categories.isEmpty());
        assertTrue(topRatedProducts.isEmpty());
        assertTrue(productMap.isEmpty());
    }

    @Test
    void Should_ReturnPaginatedProducts() {
        Pagination pagination = new Pagination().setPage(1).setLimit(1);
        when(productRepository.findAll(pagination)).thenReturn(List.of(mockProduct));

        List<Product> products = warehouse.getAllProducts(pagination);

        assertEquals(1, products.size());
        assertEquals(mockProduct, products.get(0));
    }
}