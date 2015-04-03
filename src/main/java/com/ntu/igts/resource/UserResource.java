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
import com.ntu.igts.model.User;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
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
        filterSessionContext(token, RoleEnum.USER);
        User pojo = JsonUtil.getPojoFromJsonString(inString, User.class);
        User existingUser = checkUserAvailability(pojo.getId());
        if (!existingUser.getPassword().equals(pojo.getPassword())) {
            existingUser.setPassword(pojo.getPassword());
            User updatedUser = userService.updatePassword(pojo.getId(), pojo.getPassword());
            if (updatedUser == null) {
                String[] param = { existingUser.getUserName() };
                throw new ServiceErrorException("Update user " + existingUser.getUserName() + " failed.",
                                MessageKeys.UPDATE_USER_FAILED, param);
            }
            return JsonUtil.getJsonStringFromPojo(updatedUser);
        } else {
            return JsonUtil.getJsonStringFromPojo(existingUser);
        }
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
    @Path("entity/search_term")
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
    @Path("entity/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("userid") String userId) {
        checkUserAvailability(userId);
        return JsonUtil.getJsonStringFromPojo(userService.getUserById(userId));
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
