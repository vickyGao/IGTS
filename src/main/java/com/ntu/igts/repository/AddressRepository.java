package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Address;

public interface AddressRepository extends MyRepository<Address, String> {

    @Query("from Address a where a.userId=:userId and a.deletedYN='N' order by createdTime DESC")
    public List<Address> getByUserId(@Param("userId") String userId);
}
