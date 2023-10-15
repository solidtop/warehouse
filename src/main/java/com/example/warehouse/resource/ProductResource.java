package com.example.warehouse.resource;

import com.example.warehouse.dto.*;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.interceptor.Log;
import com.example.warehouse.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Path("/products")
@Log
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
    public Response getAllProducts(@BeanParam @Valid Pagination pagination,
                                   @QueryParam("include") @DefaultValue("") String include) {
        List<Product> products = productService.getAllProducts(pagination);

        if (include.equals("metadata")) {
            Metadata metadata = productService.getMetadata(pagination);
            return Response.ok(new ProductResponse(products, metadata)).build();
        }

        return Response.ok(products).build();
    }

    @POST
    public Response addNewProduct(@Valid ProductDto productDto) {
        String name = productDto.name();
        ProductCategory category = ProductCategory.toCategory(productDto.category());
        int rating = productDto.rating();
        productService.addNewProduct(name, category, rating);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") String id) {
        return Response.ok(productService.getProductById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") String id, @Valid ProductDto productDto) {
        String name = productDto.name();
        ProductCategory category = ProductCategory.toCategory(productDto.category());
        int rating = productDto.rating();
        productService.updateProduct(id, name, category, rating);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateProductName(@PathParam("id") String id, @Valid NameDto nameDto) {
        String name = nameDto.name();
        productService.updateProduct(id, name);
        return Response.noContent().build();
    }

    @GET
    @Path("/categories")
    public Set<ProductCategory> getAllCategories() {
        return productService.getCategoriesWithProducts();
    }

    @GET
    @Path("/categories/{category}")
    public List<Product> getProductsByCategory(@PathParam("category") String category) {
        ProductCategory productCategory = ProductCategory.toCategory(category);
        return productService.getProductsByCategory(productCategory);
    }
}