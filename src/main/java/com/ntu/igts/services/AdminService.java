package com.ntu.igts.services;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Admin;
import com.ntu.igts.model.container.Query;

public interface AdminService {

    public Admin create(Admin admin);

    public Admin update(Admin admin);

    public boolean delete(String adminId);

    public Admin getAdminByAdminName(String adminName);

    public Admin getAdminDetailtById(String adminId);

    public Admin getById(String adminId);

    public Page<Admin> getPaginatedAdmins(Query query);
}
