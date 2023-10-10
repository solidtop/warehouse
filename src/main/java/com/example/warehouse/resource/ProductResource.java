package com.example.warehouse.resource;

import com.example.warehouse.dto.ProductNameDto;
import com.example.warehouse.entity.Product;
import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.dto.Pagination;
import com.example.warehouse.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
    public List<Product> getAllProducts(@BeanParam @Valid Pagination pagination) {
        return productService.getAllProducts(pagination);
    }

    @POST
    public Response addNewProduct(@Valid ProductDto productDto) {
        String name = productDto.name();
        ProductCategory category = ProductCategory.valueOf(productDto.category().toUpperCase());
        int rating = productDto.rating();
        productService.addNewProduct(name, category, rating);
        return Response.status(Response.Status.CREATED).build();
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
    public Response updateProduct(@PathParam("id") String id, @Valid ProductDto productDto) {
        String name = productDto.name();
        ProductCategory category = ProductCategory.valueOf(productDto.category().toUpperCase());
        int rating = productDto.rating();
        productService.updateProduct(id, name, category, rating);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateProductName(@PathParam("id") String id, @Valid ProductNameDto productNameDto) {
        String name = productNameDto.name();
        productService.updateProduct(id, name);
        return Response.ok().build();
    }

    @GET
    @Path("/categories")
    public Set<ProductCategory> getAllCategories() {
        return productService.getCategoriesWithProducts();
    }

    @GET
    @Path("/categories/{category}")
    public List<Product> getProductsByCategory(@PathParam("category") String category) {
        try {
            ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
            return productService.getProductsByCategory(productCategory);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}