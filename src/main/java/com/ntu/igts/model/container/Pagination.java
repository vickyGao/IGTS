package com.ntu.igts.model.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.utils.StringUtil;

@JsonRootName("pagination")
public class Pagination<T> implements Serializable {

    private static final long serialVersionUID = 550812073885725806L;

    @JsonProperty("searchterm")
    private String searchTerm = StringUtil.EMPTY;
    @JsonProperty("totalcount")
    private int totalCount;
    @JsonProperty("currentpage")
    private int currentPage;
    @JsonProperty("pagecount")
    private int pageCount;
    @JsonProperty("content")
    private List<T> content = new ArrayList<T>();

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

}
