package com.example.warehouse.dto;

import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.validation.Category;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record ProductRequest (
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Category
        String category,
        @Range(min = 0, max = 10, message = "Rating must be between 0 and 10")
        int rating)
{
}
