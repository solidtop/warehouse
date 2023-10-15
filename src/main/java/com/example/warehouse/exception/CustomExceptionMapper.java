package com.example.warehouse.exception;

import com.example.warehouse.interceptor.Log;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Collections;
import java.util.Map;

@Provider
@Log
public class CustomExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        int statusCode = e.getResponse().getStatus();
        String errorMessage = e.getMessage();
        Map<String, String> error = Collections.singletonMap("error", errorMessage);
        return Response.status(statusCode).entity(error).build();
    }
}
