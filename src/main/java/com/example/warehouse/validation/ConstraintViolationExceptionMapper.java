package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.*;

@Provider
public class ConstraintViolationExceptionMapper  implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<ErrorResponse> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : violations) {
            String field = extractFieldName(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.add(new ErrorResponse(field, message));
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