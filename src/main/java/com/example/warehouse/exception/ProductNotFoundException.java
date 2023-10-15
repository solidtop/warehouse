package com.example.warehouse.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ProductNotFoundException extends WebApplicationException {
    public ProductNotFoundException() {
        super("Product not found", Response.Status.NOT_FOUND);
    }
}
