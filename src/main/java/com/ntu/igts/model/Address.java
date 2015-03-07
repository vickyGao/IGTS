package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "address")
@JsonRootName("address")
@QueryField({})
public class Address extends BaseModel implements Serializable {

    private static final long serialVersionUID = -982553365567384192L;

    @Column(name = "address_country")
    @JsonProperty("addresscountry")
    private String addressCountry;
    @Column(name = "address_province")
    @JsonProperty("addressprovince")
    private String addressProvince;
    @Column(name = "address_city")
    @JsonProperty("addresscity")
    private String addressCity;
    @Column(name = "address_detail")
    @JsonProperty("addressdetail")
    private String addressDetail;
    @Column(name = "postcode")
    @JsonProperty("postcode")
    private String postcode;
    @Column(name = "phone_number")
    @JsonProperty("phonenumber")
    private String phoneNumber;
    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
