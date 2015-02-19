package com.ntu.igts.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntu.igts.model.SessionContext;

public interface SessionContextRepository extends MyRepository<SessionContext, String> {

    @Query("from SessionContext s where s.token=:token")
    public SessionContext getByToken(@Param("token") String token);
}
