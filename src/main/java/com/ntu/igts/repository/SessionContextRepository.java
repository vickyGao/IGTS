package com.ntu.igts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.SessionContext;

public interface SessionContextRepository extends MyRepository<SessionContext, String> {

    @Query("from SessionContext s where s.id=:token")
    public SessionContext getByToken(@Param("token") String token);

    @Query("from SessionContext s where s.userId=:userId")
    public List<SessionContext> getByUserId(@Param("userId") String userId);
}
