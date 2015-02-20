package com.ntu.igts.services.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.SessionContext;
import com.ntu.igts.repository.SessionContextRepository;
import com.ntu.igts.services.SessionContextService;

@Service
public class SessionContextServiceImpl implements SessionContextService {

    @Resource
    private SessionContextRepository sessionContextRepository;

    @Override
    @Transactional
    public SessionContext create(String userId) {
        SessionContext sessionContext = new SessionContext();
        sessionContext.setUserId(userId);
        // Set login time
        Date loginTime = new Date();
        sessionContext.setLoginTime(loginTime);
        // Set expire time by default 30 minutes
        Calendar cal = Calendar.getInstance();
        cal.setTime(loginTime);
        cal.add(Calendar.MINUTE, 30);
        Date expireTime = cal.getTime();
        sessionContext.setExpireTime(expireTime);
        return sessionContextRepository.create(sessionContext);
    }

    @Override
    @Transactional
    public SessionContext update(SessionContext sessionContext) {
        if (sessionContext.getToken() != null) {
            return sessionContextRepository.update(sessionContext);
        } else {
            return null;
        }
    }

    @Override
    public SessionContext getByToken(String token) {
        return sessionContextRepository.getByToken(token);
    }

    @Override
    @Transactional
    public SessionContext flushSessionContext(SessionContext sessionContext) {
        if (sessionContext != null) {
            // Set expire time to be 30 minutes later
            Date currentTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTime);
            cal.add(Calendar.MINUTE, 30);
            Date expireTime = cal.getTime();
            sessionContext.setExpireTime(expireTime);
            return update(sessionContext);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String token) {
        sessionContextRepository.delete(token, false);
        SessionContext sessionContext = sessionContextRepository.getByToken(token);
        if (sessionContext == null) {
            return true;
        } else {
            return false;
        }
    }

}
