package com.example.warehouse.validation;

import com.example.warehouse.dto.CategoryDTO;
import com.example.warehouse.dto.NameDTO;
import com.example.warehouse.dto.ProductDTO;
import com.example.warehouse.dto.RatingDTO;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void Should_ReturnViolation_IfNameIsBlank() {
        NameDTO nameDTO = new NameDTO("");

        Set<ConstraintViolation<NameDTO>> violations = validator.validate(nameDTO);

        assertFalse(violations.isEmpty());
    }

    @Test
    void Should_ReturnViolation_IfCategoryIsInvalid() {
        CategoryDTO categoryDTO = new CategoryDTO("invalid_category");

        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(categoryDTO);

        assertFalse(violations.isEmpty());
    }

    @Test
    void Should_ReturnViolation_IfRatingIsInvalid() {
        RatingDTO ratingDTO1 = new RatingDTO(-1);
        RatingDTO ratingDTO2 = new RatingDTO(11);

        Set<ConstraintViolation<RatingDTO>> violations1 = validator.validate(ratingDTO1);
        Set<ConstraintViolation<RatingDTO>> violations2 = validator.validate(ratingDTO2);


        assertFalse(violations1.isEmpty());
        assertFalse(violations2.isEmpty());
    }

    @Test
    void Should_ReturnViolations_IfInvalidProductDTO() {
        ProductDTO productDTO = new ProductDTO("", "invalid_category", -1);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertEquals(3, violations.size());
    }

    @Test
    void Should_ReturnViolations_IfInvalidProduct() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Product product1 = new Product("1", "", ProductCategory.MUSIC, 0, createdAt, updatedAt);
        Product product2 = new Product("2", "Product2", ProductCategory.MUSIC, -1, createdAt, updatedAt);
        Product product3 = new Product("3", "Product3", ProductCategory.MUSIC, 11, createdAt, updatedAt);

        Set<ConstraintViolation<Product>> violations1 = validator.validate(product1);
        Set<ConstraintViolation<Product>> violations2 = validator.validate(product2);
        Set<ConstraintViolation<Product>> violations3 = validator.validate(product3);

        assertFalse(violations1.isEmpty());
        assertFalse(violations2.isEmpty());
        assertFalse(violations3.isEmpty());
    }
}
