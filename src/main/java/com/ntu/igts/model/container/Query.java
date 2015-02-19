package com.ntu.igts.model.container;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.utils.StringUtil;

public class Query {

    @QueryParam("search_term")
    @DefaultValue(StringUtil.EMPTY)
    private String searchTerm;
    @QueryParam("page")
    @DefaultValue("0")
    private int page;
    @QueryParam("size")
    @DefaultValue("10")
    private int size;
    @QueryParam("sortby")
    private SortByEnum sortBy;
    @QueryParam("orderby")
    @DefaultValue("ASC")
    private OrderByEnum orderBy;
    @QueryParam("orderby")
    private double startPrice;
    @QueryParam("orderby")
    private double endPrice;
    @QueryParam("orderby")
    @DefaultValue(StringUtil.EMPTY)
    private String district;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SortByEnum getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortByEnum sortBy) {
        this.sortBy = sortBy;
    }

    public OrderByEnum getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderByEnum orderBy) {
        this.orderBy = orderBy;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
