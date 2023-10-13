package com.example.warehouse.dto;

import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.validation.Category;
import com.example.warehouse.validation.Name;
import com.example.warehouse.validation.Rating;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record ProductDto(
        @Name
        String name,
        @Category
        String category,
        @Rating
        int rating)
{
}
