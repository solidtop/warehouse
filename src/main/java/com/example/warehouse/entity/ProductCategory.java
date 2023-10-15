package com.example.warehouse.entity;

import com.example.warehouse.exception.CategoryNotFoundException;

public enum ProductCategory {
    BOOKS,
    E_BOOKS,
    MUSIC,
    VIDEO_GAMES,
    MAGAZINES;

    public static ProductCategory toCategory(String category) {
        try {
            return ProductCategory.valueOf(category.toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new CategoryNotFoundException();
        }
    }
}
