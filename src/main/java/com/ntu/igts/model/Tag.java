package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Embeddable
@Entity
@Table(name = "tag")
@JsonRootName("tag")
@QueryField({})
public class Tag extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2616479631469825693L;

    @Field(analyze = Analyze.NO)
    @Boost(1.2f)
    @Column(name = "name")
    @JsonProperty("name")
    private String name;
    @Column(name = "standard_name", unique = true, updatable = false)
    @JsonProperty("standardname")
    private String standardName;
    @Column(name = "parent_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("parentid")
    private String parentId;
    @Transient
    @JsonProperty("tags")
    private List<Tag> tags = new ArrayList<Tag>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
