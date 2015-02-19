package com.ntu.igts.model.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Tag;

@JsonRootName("queryresult")
public class CommodityQueryResult implements Serializable {

    private static final long serialVersionUID = -999763540241003011L;

    @JsonProperty("searchterm")
    private String searchTerm;
    @JsonProperty("currentpage")
    private int currentPage = 1;
    @JsonProperty("totalcount")
    private int totalCount = 0;
    @JsonProperty("totalpages")
    private int totalPages = 1;
    @JsonProperty("sortby")
    private SortByEnum sortBy;
    @JsonProperty("orderby")
    private OrderByEnum orderBy;
    @JsonProperty("startprice")
    private double startPrice;
    @JsonProperty("endprice")
    private double endPrice;
    @JsonProperty("district")
    private String district;
    @JsonProperty("content")
    private List<Commodity> content = new ArrayList<Commodity>();
    @JsonProperty("referencetags")
    private List<Tag> referenceTags = new ArrayList<Tag>();

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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

    public List<Commodity> getContent() {
        return content;
    }

    public void setContent(List<Commodity> content) {
        this.content = content;
    }

    public List<Tag> getReferenceTags() {
        return referenceTags;
    }

    public void setReferenceTags(List<Tag> referenceTags) {
        this.referenceTags = referenceTags;
    }
}
