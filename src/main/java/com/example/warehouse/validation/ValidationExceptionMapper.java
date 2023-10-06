package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<ErrorResponse> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String field = extractFieldName(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.add(new ErrorResponse(field, message));
        }

        ValidationResult result = new ValidationResult(errors);
        return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
    }

    private String extractFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}