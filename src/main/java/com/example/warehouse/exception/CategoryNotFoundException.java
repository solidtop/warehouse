package com.example.warehouse.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class CategoryNotFoundException extends WebApplicationException {
    public CategoryNotFoundException() {
        super("Category not found", Response.Status.NOT_FOUND);
    }
}
