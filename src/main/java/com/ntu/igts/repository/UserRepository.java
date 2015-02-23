package com.ntu.igts.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.User;

public interface UserRepository extends MyRepository<User, String> {

    @Query("from User u where u.userName=:userName")
    public User getUserByUserName(@Param("userName") String userName);

    @Transactional
    @Modifying
    @Query("update User u set u.password=:password where u.id=:userId")
    public int updatePassword(@Param("userId") String userId, @Param("password") String password);
}
