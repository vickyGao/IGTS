package com.ntu.igts.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "indent")
@JsonRootName("indent")
public class Indent extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3479323334885410999L;

    @Column(name = "indent_number")
    @JsonProperty("indentnumber")
    private String indentNumber; // e.g. 201502031423969130883 = year + month + date + date.getTime()
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;
    @Column(name = "user_name")
    @JsonProperty("username")
    private String userName;
    @Column(name = "indent_address")
    @JsonProperty("indentaddress")
    private String indentAddress;
    @Column(name = "pay_type")
    @JsonProperty("paytype")
    private String payType;
    @Column(name = "status")
    @JsonProperty("status")
    private String status;
    @Column(name = "indent_time")
    @JsonProperty("indenttime")
    private Date indentTime;
    @Column(name = "pay_time")
    @JsonProperty("paytime")
    private Date payTime;
    @Column(name = "deliver_time")
    @JsonProperty("delivertime")
    private Date deliverTime;
    @Column(name = "deal_complete_time")
    @JsonProperty("dealcompletetime")
    private Date dealCompleteTime;
    @Column(name = "commodity_price")
    @JsonProperty("commodityprice")
    private double commodityPrice;
    @Column(name = "commodity_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("commodityid")
    private String commodityId;
    @Column(name = "carriage")
    @JsonProperty("carriage")
    private double carriage;
    @Column(name = "indent_price")
    @JsonProperty("indentprice")
    private double indentPrice;
    @Column(name = "phone_number")
    @JsonProperty("phonenumber")
    private String phoneNumber;

    public String getIndentNumber() {
        return indentNumber;
    }

    public void setIndentNumber(String indentNumber) {
        this.indentNumber = indentNumber;
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

    public String getIndentAddress() {
        return indentAddress;
    }

    public void setIndentAddress(String indentAddress) {
        this.indentAddress = indentAddress;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getIndentTime() {
        return indentTime;
    }

    public void setIndentTime(Date indentTime) {
        this.indentTime = indentTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Date getDealCompleteTime() {
        return dealCompleteTime;
    }

    public void setDealCompleteTime(Date dealCompleteTime) {
        this.dealCompleteTime = dealCompleteTime;
    }

    public double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public double getCarriage() {
        return carriage;
    }

    public void setCarriage(double carriage) {
        this.carriage = carriage;
    }

    public double getIndentPrice() {
        return indentPrice;
    }

    public void setIndentPrice(double indentPrice) {
        this.indentPrice = indentPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
