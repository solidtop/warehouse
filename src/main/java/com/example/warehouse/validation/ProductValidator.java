package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;
import com.example.warehouse.dto.ProductRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.*;

@RequestScoped
public class ProductValidator {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public ValidationResult validate(ProductRequest productRequest) {
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        ValidationResult result = new ValidationResult();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ProductRequest> violation : violations) {
                result.addError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                );
            }
        }

        return result;
    }
}
