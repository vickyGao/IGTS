package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.AdminRole;

public interface AdminRoleRepository extends MyRepository<AdminRole, String> {

    @Query("from AdminRole a where a.adminId=:adminId and a.deletedYN='N'")
    public List<AdminRole> getAdminRolesByAdminId(@Param("adminId") String adminId);
}
