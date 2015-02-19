package com.ntu.igts.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ntu.igts.model.Admin;
import com.ntu.igts.repository.AdminRepository;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.utils.MD5Util;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminRepository adminRepository;

    @Override
    public Admin create(Admin admin) {
        admin.setAdminPassword(MD5Util.getMd5(admin.getAdminPassword()));
        return adminRepository.create(admin);
    }

    @Override
    public Admin update(Admin admin) {
        return adminRepository.update(admin);
    }

    @Override
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

}
