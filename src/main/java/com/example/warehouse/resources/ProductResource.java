package com.example.warehouse.resources;

import com.example.warehouse.entities.Product;
import com.example.warehouse.service.ProductService;
import com.example.warehouse.service.Warehouse;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    ProductService productService;

    public ProductResource(){}

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{id}")
    public Optional<Product> getProductById(@PathParam("id") String id) {
        return productService.getProductById(id);
    }
}
