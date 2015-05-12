package com.ntu.igts.model.container;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.CommodityStatusEnum;
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
    @QueryParam("startprice")
    private double startPrice;
    @QueryParam("endprice")
    private double endPrice;
    @QueryParam("district")
    @DefaultValue(StringUtil.EMPTY)
    private String district;
    @QueryParam("status")
    @DefaultValue(StringUtil.EMPTY)
    private CommodityStatusEnum status;
    @QueryParam("tagid")
    @DefaultValue(StringUtil.EMPTY)
    private String tagId;
    @QueryParam("activeyn")
    private ActiveStateEnum activeYN;

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

    public CommodityStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CommodityStatusEnum status) {
        this.status = status;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public ActiveStateEnum getActiveYN() {
        return activeYN;
    }

    public void setActiveYN(ActiveStateEnum activeYN) {
        this.activeYN = activeYN;
    }

}
