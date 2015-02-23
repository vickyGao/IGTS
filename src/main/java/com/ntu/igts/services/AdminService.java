package com.ntu.igts.services;

import com.ntu.igts.model.Admin;

public interface AdminService {

    public Admin create(Admin admin);

    public Admin update(Admin admin);

    public boolean delete(String adminId);

    public Admin getAdminByAdminName(String adminName);

    public Admin getAdminDetailtById(String adminId);

    public Admin getById(String adminId);
}
