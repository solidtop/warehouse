package com.example.warehouse.entity;

import com.example.warehouse.validation.Category;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

public record Product(
            String id,

            String name,
            ProductCategory category,
            int rating,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

}
