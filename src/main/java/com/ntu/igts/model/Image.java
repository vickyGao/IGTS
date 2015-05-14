package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "image")
@JsonRootName("image")
@QueryField({})
public class Image extends BaseModel implements Serializable {

    private static final long serialVersionUID = 4331430220570339151L;

    @Column(name = "title")
    @JsonProperty("title")
    private String title;
    @Column(name = "description")
    @JsonProperty("description")
    private String description;
    @Column(name = "uri")
    @JsonProperty("uri")
    private String uri;
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
