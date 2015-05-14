package com.ntu.igts.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ntu.igts.model.Admin;

public interface AdminRepository extends MyRepository<Admin, String> {

    @Query("from Admin a where a.adminName=:adminName and a.deletedYN='N'")
    public Admin getAdminByAdminName(@Param("adminName") String adminName);

    @Query("select count(*) from Admin t where t.deletedYN='N'")
    public int getTotalCouont();
}
