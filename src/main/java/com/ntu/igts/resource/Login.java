package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntu.igts.exception.LoginException;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.User;
import com.ntu.igts.model.container.LoginForm;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.SessionContextService;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.MD5Util;

@Component
@Path("login")
public class Login {

    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private SessionContextService sessionContextService;

    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogin(String inString) {
        LoginForm loginForm = JsonUtil.getPojoFromJsonString(inString, LoginForm.class);
        User user = userService.getUserByUserName(loginForm.getUserName());
        if (user != null) {
            if (user.getPassword().equals(MD5Util.getMd5(loginForm.getPassword()))) {
                SessionContext sessionContext = sessionContextService.create(user.getId());
                sessionContext.setUserName(user.getUserName());
                return JsonUtil.getJsonStringFromPojo(sessionContext);
            }
        }
        throw new LoginException("User name or password is wrong.", MessageKeys.USER_NAME_OR_PASSWORD_IS_WRONG);
    }

    @POST
    @Path("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String adminLogin(String inString) {
        LoginForm loginForm = JsonUtil.getPojoFromJsonString(inString, LoginForm.class);
        Admin admin = adminService.getAdminByAdminName(loginForm.getUserName());
        if (admin != null) {
            if (admin.getAdminPassword().equals(MD5Util.getMd5(loginForm.getPassword()))) {
                SessionContext sessionContext = sessionContextService.create(admin.getId());
                sessionContext.setUserName(admin.getAdminName());
                return JsonUtil.getJsonStringFromPojo(sessionContext);
            }
        }
        throw new LoginException("User name or password is wrong.", MessageKeys.USER_NAME_OR_PASSWORD_IS_WRONG);
    }

}
