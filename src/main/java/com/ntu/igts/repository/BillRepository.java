package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Bill;

public interface BillRepository extends MyRepository<Bill, String> {

    @Query("from Bill b where b.userId=:userId and b.deletedYN='N'")
    public List<Bill> getByUserId(@Param("userId") String userId);
}
