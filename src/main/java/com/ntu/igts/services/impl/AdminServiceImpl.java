package com.ntu.igts.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.AdminRole;
import com.ntu.igts.model.Role;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.AdminRepository;
import com.ntu.igts.repository.AdminRoleRepository;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.RoleService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.MD5Util;
import com.ntu.igts.utils.StringUtil;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminRepository adminRepository;
    @Resource
    private AdminRoleRepository adminRoleRepository;
    @Resource
    private RoleService roleService;

    @Override
    @Transactional
    public Admin create(Admin admin) {
        admin.setAdminPassword(MD5Util.getMd5(admin.getAdminPassword()));
        Admin insertedAdmin = adminRepository.create(admin);
        if (insertedAdmin != null) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(insertedAdmin.getId());
            List<Role> roles = roleService.getRoles();
            String roleId = CommonUtil.getRequiredRoleIdFromRoles(roles, RoleEnum.ADMIN);
            adminRole.setRoleId(roleId);
            adminRoleRepository.create(adminRole);
            return getAdminDetailtById(insertedAdmin.getId());
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Admin update(Admin admin) {
        admin.setAdminPassword(MD5Util.getMd5(admin.getAdminPassword()));
        return adminRepository.update(admin);
    }

    @Override
    @Transactional
    public boolean delete(String adminId) {
        adminRepository.delete(adminId);
        ;
        Admin admin = adminRepository.findById(adminId);
        if (admin == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Admin getAdminByAdminName(String adminName) {
        return adminRepository.getAdminByAdminName(adminName);
    }

    @Override
    public Admin getAdminDetailtById(String adminId) {
        Admin admin = adminRepository.findById(adminId);
        if (admin != null) {
            List<AdminRole> adminRoles = adminRoleRepository.getAdminRolesByAdminId(admin.getId());
            List<Role> roles = new ArrayList<Role>();
            for (AdminRole adminRole : adminRoles) {
                roles.add(roleService.getById(adminRole.getRoleId()));
                admin.setRoles(roles);
            }
        }
        return admin;
    }

    @Override
    public Admin getById(String adminId) {
        return adminRepository.findById(adminId);
    }

    @Override
    public Page<Admin> getPaginatedAdmins(Query query) {
        if (query.getSearchTerm() == null) {
            query.setSearchTerm(StringUtil.EMPTY);
        }
        if (query.getSize() == 0) {
            query.setSize(10);
        }
        if (query.getSortBy() == null) {
            query.setSortBy(SortByEnum.ADMIN_NAME);
        }
        if (query.getOrderBy() == null) {
            query.setOrderBy(OrderByEnum.ASC);
        }

        return adminRepository.findByPage(query);
    }

}
