package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "commodity_tag")
@JsonRootName("commoditytag")
@QueryField({})
public class CommodityTag extends BaseModel implements Serializable {

    private static final long serialVersionUID = -6558324138456558580L;

    @Column(name = "commodity_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("commodityid")
    private String commodityId;
    @Column(name = "tag_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("tagid")
    private String tagId;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

}
