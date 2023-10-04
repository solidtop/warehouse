package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<ErrorResponse> errors = new ArrayList<>();

    public List<ErrorResponse> getErrors() {
        return errors;
    }

    public void addError(String field, String message) {
        errors.add(new ErrorResponse(field, message));
    }
}
