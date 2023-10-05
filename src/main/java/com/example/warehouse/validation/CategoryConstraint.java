package com.example.warehouse.validation;

import com.example.warehouse.entity.ProductCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryConstraint implements ConstraintValidator<Category, String> {
    @Override
    public void initialize(Category constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            ProductCategory.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
