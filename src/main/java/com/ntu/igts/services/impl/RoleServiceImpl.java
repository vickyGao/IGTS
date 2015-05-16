package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.ntu.igts.model.Role;
import com.ntu.igts.repository.RoleRepository;
import com.ntu.igts.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        return roleRepository.create(role);
    }

    @Override
    public boolean delete(String roleId) {
        roleRepository.delete(roleId);
        Role role = roleRepository.findById(roleId);
        if (role == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Role getById(String roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
