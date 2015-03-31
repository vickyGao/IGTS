package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "admin_role")
@JsonRootName("adminrole")
@QueryField({})
public class AdminRole extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7930978676550183530L;

    @Column(name = "admin_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("adminid")
    private String adminId;
    @Column(name = "role_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("roleid")
    private String roleId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
