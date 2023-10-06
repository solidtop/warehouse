package com.example.warehouse.validation;

import com.example.warehouse.dto.ProductDto;
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

    public ValidationResult validate(ProductDto productRequest) {
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productRequest);
        ValidationResult result = new ValidationResult();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ProductDto> violation : violations) {
                result.addError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                );
            }
        }

        return result;
    }
}
