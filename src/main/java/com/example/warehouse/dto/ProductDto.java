package com.example.warehouse.dto;

import com.example.warehouse.validation.Category;
import com.example.warehouse.validation.Name;
import com.example.warehouse.validation.Rating;

public record ProductDto(
        @Name
        String name,
        @Category
        String category,
        @Rating
        int rating)
{
}
