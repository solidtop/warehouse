package com.example.warehouse.validation;


import com.example.warehouse.entity.Products;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import org.hibernate.validator.constraints.Range;

import java.lang.annotation.*;

@Constraint(
        validatedBy = {}
)
@SupportedValidationTarget({ValidationTarget.ANNOTATED_ELEMENT})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Range(min = 0, max = 10)
@ReportAsSingleViolation
public @interface Rating {
    String message() default "Rating must be between " + Products.MIN_RATING + " and " + Products.MAX_RATING;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
