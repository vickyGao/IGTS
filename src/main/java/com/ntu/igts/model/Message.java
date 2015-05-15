package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
    private String commodityId;
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
    @Column(name = "message_time", updatable = false)
    @JsonProperty("messagetime")
    private Date messageTime;
    @Transient
    @JsonProperty("messages")
    private List<Message> messages = new ArrayList<Message>();

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
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

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
