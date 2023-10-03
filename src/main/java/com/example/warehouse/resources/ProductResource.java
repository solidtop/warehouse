package com.example.warehouse.resources;

import com.example.warehouse.entities.Product;
import com.example.warehouse.entities.ProductCategory;
import com.example.warehouse.service.ProductService;
import com.example.warehouse.service.Warehouse;
import com.example.warehouse.validators.ProductValidator;
import jakarta.inject.Inject;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    private ProductService productService;

    public ProductResource() {
    }

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    public Response getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return Response.ok(products).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProduct(@Valid ProductRequest productRequest) { //TODO: Fix category conversion
        String name = productRequest.name();
        ProductCategory category = ProductCategory.valueOf(productRequest.category().toUpperCase());
        int rating = productRequest.rating();
        Product product = productService.addNewProduct(name, category, rating);
        return Response.ok(product).build();
    }

    //@PUT

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") String id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productOptional.get()).build();
    }

    @GET
    @Path("/categories")
    public Response getAllCategories() {
        Set<ProductCategory> categories = productService.getCategoriesWithProducts();
        return Response.ok(categories).build();
    }

    @GET
    @Path("/categories/{category}")
    public Response getProductsByCategory(@PathParam("category") String category) {
        try {
            ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
            List<Product> products = productService.getProductsByCategory(productCategory);
            return Response.ok(products).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}