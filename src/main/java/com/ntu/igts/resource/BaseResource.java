package com.ntu.igts.resource;

import java.util.Date;

import javax.annotation.Resource;

import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.UnAuthorizedException;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.User;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.SessionContextService;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.CommonUtil;

public class BaseResource {

    @Resource
    protected SessionContextService sessionContextService;
    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;

    /**
     * Get sessionContext by token
     * 
     * @param token
     *            The id of sessionContext
     * @param allowedRoles
     *            Roles which are allowed to access
     * @return If the user has the exact role and its session is not expired, return sessionContext, or throw exception
     */
    protected SessionContext filterSessionContext(String token, RoleEnum allowedRole) {
        SessionContext sessionContext = sessionContextService.getByToken(token);
        if (sessionContext != null) { // If the session exists
            if (RoleEnum.ALL.equals(allowedRole)) { // If the session exits and all roles are allowed
                flushSessionContext(token);
                return null;
            } else {
                if (!isSessionContextExpired(sessionContext)) { // If the session exists, but only specify user is
                                                                // allowed
                    if (RoleEnum.USER.equals(allowedRole)) { // Only allow user
                        User user = userService.getUserDetailById(sessionContext.getUserId());
                        if (CommonUtil.isRoleAllowed(user.getRoles(), allowedRole)) {
                            flushSessionContext(token);
                            sessionContext.setUserId(user.getId());
                            sessionContext.setUserName(user.getUserName());
                            return sessionContext;
                        }
                    } else if (RoleEnum.ADMIN.equals(allowedRole)) { // Only allow admin
                        Admin admin = adminService.getAdminDetailtById(sessionContext.getUserId());
                        if (CommonUtil.isRoleAllowed(admin.getRoles(), allowedRole)) {
                            flushSessionContext(token);
                            sessionContext.setUserId(admin.getId());
                            sessionContext.setUserName(admin.getAdminName());
                            return sessionContext;
                        }
                    }
                }
            }
        } else if (RoleEnum.ALL.equals(allowedRole)) { // If the session does not exist and all roles are allowed
            return null;
        }
        throw new UnAuthorizedException("Error 401 Unauthorized", MessageKeys.UNAUTHORIZED);
    }

    protected void flushSessionContext(String token) {
        SessionContext sessionContext = sessionContextService.getByToken(token);
        sessionContextService.flushSessionContext(sessionContext);
    }

    private boolean isSessionContextExpired(SessionContext sessionContext) {
        Date date = new Date();
        if (date.getTime() < sessionContext.getExpireTime().getTime()) {
            return false;
        } else {
            sessionContextService.delete(sessionContext.getToken());
            return true;
        }
    }
}
