package com.example.warehouse.validation;

import com.example.warehouse.entity.ProductCategory;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@NotNull
public @interface Category {
    Class<? extends Enum<?>> category() default ProductCategory.class;
    String message() default "Category not found";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
