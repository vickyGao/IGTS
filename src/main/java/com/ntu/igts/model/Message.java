package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "message")
@JsonRootName("message")
@QueryField({})
public class Message extends BaseModel implements Serializable {

    private static final long serialVersionUID = 2122366791637631002L;

    @Column(name = "commodity_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("commodityid")
    private String CommodityId;
    @Column(name = "content")
    @JsonProperty("content")
    private String content;
    @Column(name = "floor")
    @JsonProperty("floor")
    private int floor;
    @Column(name = "parent_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("parentid")
    private String parentId;
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;
    @Column(name = "user_name")
    @JsonProperty("username")
    private String userName;

    public String getCommodityId() {
        return CommodityId;
    }

    public void setCommodityId(String commodityId) {
        CommodityId = commodityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
