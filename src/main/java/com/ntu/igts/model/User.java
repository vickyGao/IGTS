package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "user")
@JsonRootName("user")
@QueryField({ "userName" })
public class User extends BaseModel implements Serializable {

    private static final long serialVersionUID = 5852161501955587332L;

    @Column(name = "user_name", updatable = false)
    @JsonProperty("username")
    private String userName;
    @Column(name = "password")
    @JsonProperty("password")
    private String password;
    @Column(name = "real_name")
    @JsonProperty("realname")
    private String realName;
    @Column(name = "email")
    @JsonProperty("email")
    private String email;
    @Column(name = "phone_number")
    @JsonProperty("phonenumber")
    private String phoneNumber;
    @Column(name = "gender")
    @JsonProperty("gender")
    private String gender;
    @Column(name = "age")
    @JsonProperty("age")
    private int age;
    @Column(name = "active_yn", columnDefinition = "VARCHAR(2)")
    @JsonProperty("activeyn")
    private String activeYN = "Y";
    @Column(name = "seller_level")
    @JsonProperty("sellerlevel")
    private int sellerLevel;
    @Column(name = "buyer_level")
    @JsonProperty("buyerlevel")
    private int buyerLevel;
    @Column(name = "seller_exp")
    @JsonProperty("sellerexp")
    private int sellerExp;
    @Column(name = "buyer_exp")
    @JsonProperty("buyerexp")
    private int buyerExp;
    @Column(name = "id_number")
    @JsonProperty("idnumber")
    private String idNumber; // id card number
    @Column(name = "money")
    @JsonIgnore
    private double money;
    @Transient
    @JsonProperty("roles")
    private List<Role> roles = new ArrayList<Role>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public void setActiveYN(String activeYN) {
        this.activeYN = activeYN;
    }

    public int getSellerLevel() {
        return sellerLevel;
    }

    public void setSellerLevel(int sellerLevel) {
        this.sellerLevel = sellerLevel;
    }

    public int getBuyerLevel() {
        return buyerLevel;
    }

    public void setBuyerLevel(int buyerLevel) {
        this.buyerLevel = buyerLevel;
    }

    public int getSellerExp() {
        return sellerExp;
    }

    public void setSellerExp(int sellerExp) {
        this.sellerExp = sellerExp;
    }

    public int getBuyerExp() {
        return buyerExp;
    }

    public void setBuyerExp(int buyerExp) {
        this.buyerExp = buyerExp;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
