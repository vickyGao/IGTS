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

@Entity
@Table(name = "admin")
@JsonRootName("admin")
public class Admin extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2592865393997208669L;

    @Column(name = "admin_name", unique = true, updatable = false)
    @JsonProperty("adminname")
    private String adminName;
    @Column(name = "admin_password")
    @JsonProperty("adminpassword")
    private String adminPassword;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
