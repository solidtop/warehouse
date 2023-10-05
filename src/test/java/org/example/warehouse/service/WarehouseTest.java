package org.example.warehouse.service;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.service.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {
    private Clock fixedClock;
    private LocalDateTime now;
    private Product mockProduct;
    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        now = LocalDateTime.now(fixedClock);
        mockProduct = new Product("1", "Product1", ProductCategory.BOOKS, 0, now, now);
        mockProducts = Arrays.asList(
                mockProduct,
                new Product("2", "Product2", ProductCategory.BOOKS, 0, now, now)
        );
    }

    @Test
    void Should_AddNewProduct() {
        Warehouse warehouse = new Warehouse(fixedClock);

        Product product = warehouse.addNewProduct("TestProduct", ProductCategory.BOOKS, 5);

        assertNotNull(product);
        assertEquals("TestProduct", product.name());
        assertEquals(ProductCategory.BOOKS, product.category());
        assertEquals(5, product.rating());
        assertNotNull(product.id());
        assertEquals(now, product.createdAt());
        assertEquals(now, product.updatedAt());
    }

    @Test
    void Should_ThrowException_IfNameIsEmpty() {
        Warehouse warehouse = new Warehouse();

        assertThrows(IllegalArgumentException.class, () ->
                warehouse.addNewProduct("", ProductCategory.BOOKS, 5));
    }

    @Test
    void Should_ThrowException_IfRatingIsInvalid() {
        Warehouse warehouse = new Warehouse();

        assertThrows(IllegalArgumentException.class, () ->
                warehouse.addNewProduct("Product", ProductCategory.BOOKS, -1));
        assertThrows(IllegalArgumentException.class, () ->
                warehouse.addNewProduct("Product", ProductCategory.BOOKS, 11));
    }

    @Test
    void Should_ReturnAllProducts() {
        Warehouse warehouse = new Warehouse(mockProducts);

        List<Product> products = warehouse.getAllProducts();

        int expected = 2;
        int actual = products.size();
        assertEquals(expected, actual);
    }

    @Test
    void Should_ReturnProduct_IfExists() {
        Warehouse warehouse = new Warehouse(mockProducts);

        Optional<Product> productOptional = warehouse.getProductById(mockProduct.id());

        assertTrue(productOptional.isPresent());
        assertEquals(mockProduct, productOptional.get());
    }

    @Test
    void Should_ReturnEmptyOptional_IfProductNotExists() {
        Warehouse warehouse = new Warehouse();

        Optional<Product> productOptional = warehouse.getProductById(mockProduct.id());

        assertTrue(productOptional.isEmpty());
    }

    @Test
    void Should_UpdateProductName() {
        Warehouse warehouse = new Warehouse(mockProducts);

        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), "UpdatedProduct");

        String expected = "UpdatedProduct";
        String actual = updatedProduct.name();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateProductCategory() {
        Warehouse warehouse = new Warehouse(mockProducts);

        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), ProductCategory.MUSIC);

        ProductCategory expected = ProductCategory.MUSIC;
        ProductCategory actual = updatedProduct.category();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateProductRating() {
        Warehouse warehouse = new Warehouse(mockProducts);

        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), 5);

        int expected = 5;
        int actual = updatedProduct.rating();
        assertEquals(expected, actual);
    }

    @Test
    void Should_UpdateAllProductDetails() {
        LocalDateTime createdAt = now.minusMinutes(1);
        Product mockProduct = new Product("1", "Product", ProductCategory.BOOKS, 2, createdAt, createdAt);
        Warehouse warehouse = new Warehouse(Arrays.asList(mockProduct), fixedClock);

        Product updatedProduct = warehouse.updateProduct(mockProduct.id(), "UpdatedProduct", ProductCategory.VIDEO_GAMES, 10);

        assertEquals("UpdatedProduct", updatedProduct.name());
        assertEquals(ProductCategory.VIDEO_GAMES, updatedProduct.category());
        assertEquals(10, updatedProduct.rating());
        assertTrue(updatedProduct.updatedAt().isAfter(createdAt));
    }

    @Test
    void Should_ThrowException_IfInvalidUpdateProductDetails() {
        Warehouse warehouse = new Warehouse(mockProducts);

        assertThrows(NoSuchElementException.class, () ->
                warehouse.updateProduct("abc", "UpdatedProduct"));
        assertThrows(IllegalArgumentException.class, () ->
                warehouse.updateProduct(mockProduct.id(), ""));
        assertThrows(IllegalArgumentException.class, () ->
                warehouse.updateProduct(mockProduct.id(), -1));
        assertThrows(IllegalArgumentException.class, () ->
                warehouse.updateProduct(mockProduct.id(), 11));
    }

    @Test
    void Should_ReturnProductsInCategorySortedByAlphabeticalOrder() {
        Warehouse warehouse = new Warehouse(List.of(
                new Product("1", "DFG", ProductCategory.BOOKS, 5, now, now),
                new Product("2", "HIJ", ProductCategory.MUSIC, 6, now, now),
                new Product("3", "ABC", ProductCategory.BOOKS, 8, now, now)
        ));

        List<Product> booksProducts = warehouse.getProductsByCategory(ProductCategory.BOOKS);

        int expected = 2;
        int actual = booksProducts.size();
        assertEquals(expected, actual);
        assertEquals('A', booksProducts.get(0).name().charAt(0));
    }

    @Test
    void Should_ReturnProductsSinceSpecifiedDate() {
        Warehouse warehouse = new Warehouse(List.of(
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
        Warehouse warehouse = new Warehouse(List.of(
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
        Warehouse warehouse = new Warehouse(List.of(
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
        Warehouse warehouse = new Warehouse(List.of(
                new Product("1", "Product1", ProductCategory.BOOKS, 8, now, now),
                new Product("2", "Product2", ProductCategory.VIDEO_GAMES, 4, now, now),
                new Product("3", "Product3", ProductCategory.BOOKS, 3, now, now)
        ));

        long productCount = warehouse.getProductCountInCategory(ProductCategory.BOOKS);

        int expected = 2;
        assertEquals(expected, productCount);
    }

    @Test
    void Should_ReturnProductsWithMaxRatingThisMonthSortedByNewest() {
        Warehouse warehouse = new Warehouse(List.of(
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
        Warehouse warehouse = new Warehouse(List.of(
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
        Warehouse warehouse = new Warehouse();

        List<Product> allProducts = warehouse.getAllProducts();
        List<Product> booksProducts = warehouse.getProductsByCategory(ProductCategory.BOOKS);
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
}