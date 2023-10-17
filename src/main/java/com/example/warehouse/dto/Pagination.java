package com.example.warehouse.dto;

import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

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

    public Pagination setPage(long page) {
        this.page = page;
        return this;
    }

    public long getLimit() {
        return limit;
    }

    public Pagination setLimit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public String toString() {
        return "Pagination[" +
                "page=" + page + ", " +
                "limit=" + limit + ']';
    }


}
