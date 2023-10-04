package com.example.warehouse.resource;

import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.ProductRequest;
import com.example.warehouse.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
    public Response addNewProduct(@Valid ProductRequest productRequest) {
        String name = productRequest.name();
        ProductCategory category = ProductCategory.valueOf(productRequest.category().toUpperCase());
        int rating = productRequest.rating();
        Product product = productService.addNewProduct(name, category, rating);
        return Response.ok(product).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") String id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(productOptional.get()).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") String id, @Valid ProductRequest productRequest) {
        try {
            String name = productRequest.name();
            ProductCategory category = ProductCategory.valueOf(productRequest.category().toUpperCase());
            int rating = productRequest.rating();
            Product product = productService.updateProduct(id, name, category, rating);
            return Response.ok(product).build();
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No product found").build();
        }
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