package com.ntu.igts.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.annotations.QueryField;

@Entity
@Table(name = "user_role")
@JsonRootName("userrole")
@QueryField({})
public class UserRole extends BaseModel implements Serializable {

    private static final long serialVersionUID = -5112899731440010311L;

    @Column(name = "user_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("userid")
    private String userId;
    @Column(name = "role_id", columnDefinition = "VARCHAR(36)")
    @JsonProperty("roleid")
    private String roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
