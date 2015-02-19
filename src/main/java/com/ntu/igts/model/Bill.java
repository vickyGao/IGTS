package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "bill")
@JsonRootName("bill")
public class Bill extends BaseModel implements Serializable {

    private static final long serialVersionUID = 7529131224929254388L;

    @Column(name = "content")
    @JsonProperty("content")
    private String content;
    @Column(name = "amount")
    @JsonProperty("amount")
    private String amount; // pay: '-', achieve: '+'
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
