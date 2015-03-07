package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.UserRole;

public interface UserRoleRepository extends MyRepository<UserRole, String> {

    @Query("from UserRole u where u.userId=:userId and u.deletedYN='N'")
    public List<UserRole> getUserRolesByUserId(@Param("userId") String userId);
}
