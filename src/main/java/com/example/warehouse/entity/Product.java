package com.example.warehouse.entity;

import com.example.warehouse.validation.Category;
import com.example.warehouse.validation.Name;
import com.example.warehouse.validation.Rating;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Product(
            @NotNull
            String id,
            @Name
            String name,
            ProductCategory category,
            @Rating
            int rating,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

}
