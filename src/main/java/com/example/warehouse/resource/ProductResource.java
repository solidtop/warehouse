package com.example.warehouse.resource;

import com.example.warehouse.dto.*;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.interceptor.Log;
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
    public Response addNewProduct(@Valid ProductDTO productDto) {
        productService.addNewProduct(productDto);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") String id) {
        return Response.ok(productService.getProductById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") String id, @Valid ProductDTO productDto) {
        productService.updateProduct(id, productDto);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateProductName(@PathParam("id") String id, @Valid NameDTO nameDto) {
        productService.updateProduct(id, nameDto);
        return Response.noContent().build();
    }

    @GET
    @Path("/categories")
    public Set<ProductCategory> getAllCategories() {
        return productService.getCategoriesWithProducts();
    }

    @GET
    @Path("/categories/{category}")
    public Response getProductsByCategory(@PathParam("category") String category,
                                          @BeanParam @Valid Pagination pagination,
                                          @QueryParam("include") @DefaultValue("") String include){
        List<Product> products = productService.getProductsByCategory(category, pagination);
        if (include.equals("metadata")) {
            Metadata metadata = productService.getMetadata(pagination);
            return Response.ok(new ProductResponse(products, metadata)).build();
        }

        return Response.ok(products).build();
    }
}