package com.example.warehouse.resource;

import com.example.warehouse.dto.ProductDto;
import com.example.warehouse.entity.Product;
import com.example.warehouse.entity.ProductCategory;
import com.example.warehouse.exception.CustomExceptionMapper;
import com.example.warehouse.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {
    @Mock
    ProductService productService;
    Dispatcher dispatcher;
    Product mockProduct;
    List<Product> products;

    @BeforeEach
    void setUp() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var productResource = new ProductResource(productService);
        dispatcher.getRegistry().addSingletonResource(productResource);
        dispatcher.getProviderFactory().registerProvider(CustomExceptionMapper.class);

        mockProduct = new Product("1", "Product1", ProductCategory.MUSIC, 5, LocalDateTime.now(), LocalDateTime.now());
        products = List.of(
                mockProduct,
                new Product("2", "Product2", ProductCategory.MUSIC, 5, LocalDateTime.now(), LocalDateTime.now()),
                new Product("3", "Product3", ProductCategory.MUSIC, 5, LocalDateTime.now(), LocalDateTime.now())
        );
    }

    @Test
    void Should_RespondWithStatus200_When_RequestingProducts() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/products");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    void Should_RespondWithEmptyList_When_NoProductsExists() {
//        MockHttpRequest request = MockHttpRequest.get("/products");
//        MockHttpResponse response = new MockHttpResponse();
    }

    @Test
    void Should_RespondWithProductList_When_ProductsExists() {
        
    }

    @Test
    void Should_RespondWithPaginatedProducts() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/products?page=2&limit=1");
        MockHttpResponse response = new MockHttpResponse();
        when(productService.getAllProducts(any())).thenReturn(products);

        dispatcher.invoke(request, response);

        ObjectMapper objectMapper = new ObjectMapper();

        assertEquals("asd", response.getContentAsString());
    }

    @Test
    void Should_RespondWithMetadataIncluded() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/products?include=metadata");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        String expected = "metadata";
        String actual = response.getContentAsString();
        assertTrue(actual.contains(expected));
    }

    @Test
    void Should_ReturnStatus200_When_ProductExists() throws Exception {
        when(productService.getProductById("1")).thenReturn(mockProduct);
        MockHttpRequest request = MockHttpRequest.get("/products/1");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("asd", response.getContentAsString());
    }

    @Test
    void Should_RespondWithStatus404_IfProductNotFound() throws Exception {
        //when(productService.getProductById("1")).thenThrow(ProductNotFoundException.class);
        MockHttpRequest request = MockHttpRequest.get("/products/1");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(404, response.getStatus());
       // assertEquals("Product not found", response.getContentAsString());
    }

    @Test
    void Should_RespondWithStatus201_When_AddingNewProduct() throws Exception {
        MockHttpRequest request = MockHttpRequest.post("/products");
        ProductDto productDto = new ProductDto("NewProduct", "books", 1);
        String json = new ObjectMapper().writeValueAsString(productDto);
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(201, response.getStatus());
    }

    @Test
    void Should_RespondWith200_When_UpdatingExistingProduct() throws Exception {
        MockHttpRequest request = MockHttpRequest.put("/products/-1");
        ProductDto productDto = new ProductDto("UpdatedProduct", "books", 10);
        String json = new ObjectMapper().writeValueAsString(productDto);
        request.content(json.getBytes());
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
    }
}