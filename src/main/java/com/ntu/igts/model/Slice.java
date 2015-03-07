package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "slice")
@JsonRootName("slice")
@QueryField({})
public class Slice extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7737152560739189189L;

    @Column(name = "image_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("imageid")
    private String imageId;
    @Column(name = "description")
    @JsonProperty("description")
    private String description;
    @Column(name = "display_sequence")
    @JsonProperty("displaysequence")
    private int displaySequence;
    @Column(name = "commodity_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("commodityid")
    private String commodityId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplaySequence() {
        return displaySequence;
    }

    public void setDisplaySequence(int displaySequence) {
        this.displaySequence = displaySequence;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

}
