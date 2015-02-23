package com.ntu.igts.resource;

import java.util.Date;

import javax.annotation.Resource;

import com.ntu.igts.exception.AuthenticationException;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.User;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.SessionContextService;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.CommonUtil;

public class BaseResource {

    @Resource
    private SessionContextService sessionContextService;
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;

    protected SessionContext getUserSessionContext(String token, String... allowedRoles) {
        SessionContext sessionContext = sessionContextService.getByToken(token);
        if (sessionContext != null) {
            Date date = new Date();
            if (date.getTime() < sessionContext.getExpireTime().getTime()) {
                User user = userService.getUserDetailById(sessionContext.getUserId());
                if (CommonUtil.isRoleAllowed(user.getRoles(), allowedRoles)) {
                    sessionContext = sessionContextService.flushSessionContext(sessionContext);
                    sessionContext.setUser(user);
                    return sessionContext;
                } else {
                    throw new AuthenticationException("403");
                }
            }
        }
        throw new AuthenticationException("401");
    }

    protected SessionContext getAdminSessionContext(String token, String... allowedRoles) {
        SessionContext sessionContext = sessionContextService.getByToken(token);
        if (sessionContext != null) {
            Date date = new Date();
            if (date.getTime() < sessionContext.getExpireTime().getTime()) {
                Admin admin = adminService.getAdminDetailtById(sessionContext.getUserId());
                if (CommonUtil.isRoleAllowed(admin.getRoles(), allowedRoles)) {
                    sessionContext = sessionContextService.flushSessionContext(sessionContext);
                    sessionContext.setAdmin(admin);
                    return sessionContext;
                } else {
                    throw new AuthenticationException("403");
                }
            }
        }
        throw new AuthenticationException("401");
    }

}
