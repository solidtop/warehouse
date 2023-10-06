package com.example.warehouse.validation;

import com.example.warehouse.dto.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public record ValidationResult(List<ErrorResponse> errors) {

}
