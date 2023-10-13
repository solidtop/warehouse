package com.example.warehouse.dto;

import com.example.warehouse.entity.Product;

import java.util.List;

public record ProductResponse(List<Product> products, Metadata metadata) {
}
