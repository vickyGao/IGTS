package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "custom_module")
@JsonRootName("custommodule")
@QueryField({})
public class CustomModule extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2984260644374625063L;

    @Column(name = "title")
    @JsonProperty("title")
    private String title;
    @Column(name = "display_sequence")
    @JsonProperty("displaysequence")
    private int displaySequence;
    @Column(name = "keyword")
    @JsonProperty("keyword")
    private String keyword;
    @Column(name = "display_amount")
    @JsonProperty("displayamount")
    private int displayAmount;
    @Transient
    @JsonProperty("commodities")
    private List<Commodity> commodities = new ArrayList<Commodity>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(int displaySequence) {
        this.displaySequence = displaySequence;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(int displayAmount) {
        this.displayAmount = displayAmount;
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<Commodity> commodities) {
        this.commodities = commodities;
    }

}
