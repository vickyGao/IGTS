package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Role;

public interface RoleService {

    public Role create(Role role);

    public boolean delete(String roleId);

    public Role getById(String roleId);

    public List<Role> getRoles();
}
