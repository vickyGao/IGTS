package com.ntu.igts.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ntu.igts.model.SessionContext;
import com.ntu.igts.repository.SessionContextRepository;
import com.ntu.igts.services.SessionContextService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.StringUtil;

@Service
public class SessionContextServiceImpl implements SessionContextService {

    private static final Logger LOGGER = Logger.getLogger(SessionContextServiceImpl.class);

    @Resource
    private SessionContextRepository sessionContextRepository;

    @Override
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
    public SessionContext update(SessionContext sessionContext) {
        if (sessionContext.getToken() != null) {
            return sessionContextRepository.update(sessionContext);
        } else {
            return null;
        }
    }

    @Override
    public SessionContext getByToken(String token) {
        if (!StringUtil.isEmpty(token)) {
            token = CommonUtil.getFormattedToken(token);
            SessionContext sessionContext = sessionContextRepository.findById(token, true);
            if(sessionContext == null){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOGGER.warn("Sleep 2s to get sessionContext");;
                }
                sessionContext = sessionContextRepository.findById(token, true);
            }
            return sessionContext;
        } else {
            return null;
        }
    }

    @Override
    public SessionContext getUserByToken(String token) {
        if (!StringUtil.isEmpty(token)) {
            token = CommonUtil.getFormattedToken(token);
            return sessionContextRepository.findById(token, true);
        }else{
            return null;
        }
    }

    @Override
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
    public boolean delete(String token) {
        token = CommonUtil.getFormattedToken(token);
        sessionContextRepository.delete(token, false);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOGGER.warn("Sleep 2s to get sessionContext");;
        }
        SessionContext sessionContext = getUserByToken(token);
        if (sessionContext == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SessionContext getByUserId(String userId) {
        List<SessionContext> sessionContextList = sessionContextRepository.getByUserId(userId);
        if (sessionContextList.size() == 0) {
            return null;
        } else if (sessionContextList.size() == 1) {
            return sessionContextList.get(0);
        } else {
            LOGGER.warn("Duplicate session context exists");
            return sessionContextList.get(0);
        }
    }

}
