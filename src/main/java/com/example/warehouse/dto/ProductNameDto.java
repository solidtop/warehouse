package com.example.warehouse.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductNameDto(@NotBlank String name) {
}
