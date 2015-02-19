package com.ntu.igts.services;

import com.ntu.igts.model.SessionContext;

public interface SessionContextService {

    public SessionContext create(String userId);

    public SessionContext update(SessionContext sessionContext);

    public SessionContext getByToken(String token);

    public SessionContext flushSessionContext(SessionContext sessionContext);

    public boolean delete(String token);
}
