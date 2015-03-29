package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "role")
@JsonRootName("role")
@QueryField({})
public class Role extends BaseModel implements Serializable {

    private static final long serialVersionUID = 7981046572876956082L;

    @Column(name = "role_name")
    @JsonProperty("rolename")
    private String roleName;
    @Column(name = "role_standard_name", unique = true, updatable = false)
    @JsonProperty("rolestandardname")
    private String roleStandardName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleStandardName() {
        return roleStandardName;
    }

    public void setRoleStandardName(String roleStandardName) {
        this.roleStandardName = roleStandardName;
    }

}
