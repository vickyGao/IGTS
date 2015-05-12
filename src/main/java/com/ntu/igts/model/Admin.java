package com.ntu.igts.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "admin")
@JsonRootName("admin")
@QueryField({ "adminName" })
public class Admin extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2592865393997208669L;

    @Column(name = "admin_name", updatable = false)
    @JsonProperty("adminname")
    private String adminName;
    @Column(name = "admin_password")
    @JsonProperty("adminpassword")
    private String adminPassword;
    @Transient
    @JsonProperty("newpassword")
    private String newPassword;
    @Transient
    @JsonProperty("newpwd1")
    private String newPwd1;
    @Transient
    @JsonProperty("newpwd2")
    private String newPwd2;
    @Transient
    @JsonProperty("roles")
    private List<Role> roles = new ArrayList<Role>();

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPwd1() {
        return newPwd1;
    }

    public void setNewPwd1(String newPwd1) {
        this.newPwd1 = newPwd1;
    }

    public String getNewPwd2() {
        return newPwd2;
    }

    public void setNewPwd2(String newPwd2) {
        this.newPwd2 = newPwd2;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
