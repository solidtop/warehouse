package com.example.warehouse.dto;

import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

import java.util.Objects;

public final class Pagination {

    @QueryParam("page")
    @DefaultValue("1")
    @Positive
    private long page;
    @QueryParam("limit")
    @DefaultValue("10")
    @Positive
    private long limit;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Pagination[" +
                "page=" + page + ", " +
                "limit=" + limit + ']';
    }


}
