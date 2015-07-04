package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.User;
import com.ntu.igts.model.container.AccountContainer;
import com.ntu.igts.model.container.Asset;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.MD5Util;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("user")
public class UserResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private UserService userService;
    @Resource
    private MessageBuilder messageBuilder;

    /**
     * Create a new user (register)
     * 
     * @param inString
     *            The post body of user pojo
     * @return The created user
     */
    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String inString) {
        User pojo = JsonUtil.getPojoFromJsonString(inString, User.class);
        User existUser = userService.getUserByUserName(pojo.getUserName());
        if(existUser != null){
            throw new ServiceWarningException("The username was registered", MessageKeys.USERNAME_REGISTERED);
        }
        User user = userService.create(pojo);
        if (user == null) {
            String[] param = { pojo.getUserName() };
            throw new ServiceErrorException("Create user " + pojo.getUserName() + " failed.",
                            MessageKeys.CREATE_USER_FAILED, param);
        }
        return JsonUtil.getJsonStringFromPojo(user);
    }

    /**
     * Update user's password
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The json put body of user pojo
     * @return The updated user
     */
    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        User pojo = JsonUtil.getPojoFromJsonString(inString, User.class);
        User existingUser = checkUserAvailability(sessionContext.getUserId());
        if (!existingUser.getPassword().equals(MD5Util.getMd5(pojo.getPassword()))) {
            throw new ServiceWarningException("Origin password is not correct", MessageKeys.ORIGIN_PASSWORD_WRONG);
        }

        User updatedUser = userService.updatePassword(pojo.getId(), pojo.getNewPassword());
        if (updatedUser == null) {
            String[] param = { existingUser.getUserName() };
            throw new ServiceErrorException("Update user " + existingUser.getUserName() + " failed.",
                            MessageKeys.UPDATE_USER_FAILED, param);
        }
        return JsonUtil.getJsonStringFromPojo(updatedUser);

    }

    /**
     * Update user's detail info (realName, age, etc.)
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The json put body of user pojo
     * @return The updated user
     */
    @PUT
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateDetail(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        User pojo = JsonUtil.getPojoFromJsonString(inString, User.class);
        User existingUser = checkUserAvailability(pojo.getId());
        if (StringUtil.isEmpty(pojo.getPassword())) {
            pojo.setPassword(existingUser.getPassword());
        }
        pojo.setLockedMoney(existingUser.getLockedMoney());
        pojo.setMoney(existingUser.getMoney());
        User returnUser = userService.update(pojo);
        return JsonUtil.getJsonStringFromPojo(returnUser);
    }

    /**
     * Update user's active state (from active to negative etc.)
     * 
     * @param token
     *            The sessionContext id
     * @param activeStateEnum
     *            ACTIVE or NEGATIVE
     * @param userId
     *            The id of the user
     * @return The updated user
     */
    @PUT
    @Path("entity/{activeyn}/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserActiveYnState(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("activeyn") ActiveStateEnum activeStateEnum, @PathParam("userid") String userId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        User existingUser = checkUserAvailability(userId);
        existingUser.setActiveYN(activeStateEnum.value());
        User returnUser = userService.update(existingUser);
        return JsonUtil.getJsonStringFromPojo(returnUser);
    }

    /**
     * Delete the user
     * 
     * @param token
     *            The sessionContext id
     * @param userId
     *            The id of the user
     * @return If succeed to delete the user, will return success, or return fail
     */
    @DELETE
    @Path("entity/{userid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("userid") String userId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkUserAvailability(userId);
        boolean flag = userService.delete(userId);
        String[] param = { userId };
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_USER_FOR_ID_SUCCESS, param, "Delete user " + userId
                            + " success.", CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_USER_FOR_ID_FAIL, param, "Delete user " + userId
                            + " fail.", CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    /**
     * Get users by page
     * 
     * @param token
     *            The sessionContext id
     * @param query
     *            The query parameters, like searchTerm, sortBy, orderBy etc.
     * @return The pagination of users
     */
    @GET
    @Path("search_term")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserByPage(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @BeanParam Query query) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Page<User> page = userService.getByPage(query);
        Pagination<User> pagination = new Pagination<User>();
        pagination.setSearchTerm(query.getSearchTerm());
        pagination.setCurrentPage(0);
        pagination.setTotalCount((int) page.getTotalElements());
        pagination.setPageCount(page.getTotalPages());
        pagination.setContent(page.getContent());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    @GET
    @Path("detail/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserDetailById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("userid") String userId) {
        filterSessionContext(token, RoleEnum.USER);
        return JsonUtil.getJsonStringFromPojo(userService.getUserDetailById(userId));
    }

    @GET
    @Path("admin/detail/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserDetailByIdForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("userid") String userId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(userService.getUserDetailById(userId));
    }

    @GET
    @Path("detail/token")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserByToken(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = sessionContextService.getByToken(token);
        User user = null;
        if (sessionContext != null) {
            user = userService.getUserDetailById(sessionContext.getUserId());
        }
        if (user != null) {
            return JsonUtil.getJsonStringFromPojo(user);
        } else {
            return JsonUtil.getJsonStringFromPojo(new User());
        }
    }

    @GET
    @Path("asset")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssetByUserId(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        User user = userService.getUserById(sessionContext.getUserId());
        Asset asset = new Asset();
        if (user != null) {
            asset.setMoney(user.getMoney());
            asset.setLockedMoney(user.getLockedMoney());
            asset.setUserId(user.getId());
        }

        return JsonUtil.getJsonStringFromPojo(asset);
    }

    @GET
    @Path("totalcount")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalCount(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return userService.getTotalCount();
    }

    @GET
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserAccount(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        User existingUser = checkUserAvailability(sessionContext.getUserId());
        AccountContainer account = new AccountContainer();
        account.setTotalMoney(existingUser.getMoney());
        account.setLockedMoney(existingUser.getLockedMoney());
        return JsonUtil.getJsonStringFromPojo(account);
    }

    private User checkUserAvailability(String userId) {
        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            String[] param = { userId };
            throw new ServiceWarningException("Cannot find user for id " + userId, MessageKeys.USER_NOT_FOUND_FOR_ID,
                            param);
        } else {
            return existingUser;
        }
    }

}
