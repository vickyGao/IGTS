package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.Indent;

public interface IndentRepository extends MyRepository<Indent, String> {

    @Query("from Indent i where i.userId=:userId and i.deletedYN='N'")
    public List<Indent> getByUserId(@Param("userId") String userId);
}
