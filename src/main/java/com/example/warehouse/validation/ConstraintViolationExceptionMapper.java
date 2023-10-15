package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;
import com.example.warehouse.resource.ProductResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Provider
public class ConstraintViolationExceptionMapper  implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<ErrorResponse> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : violations) {
            String field = extractFieldName(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.add(new ErrorResponse(field, message));
            logger.error("Validation error for field: {} - Message: {}", field, message);
        }
        Map<String, List<ErrorResponse>> response = new HashMap<>();
        response.put("errors", errors);
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    private String extractFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}