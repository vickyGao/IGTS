package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Hot;
import com.ntu.igts.model.container.HotList;
import com.ntu.igts.services.HotService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("hot")
public class HotResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private HotService hotService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Hot pojo = JsonUtil.getPojoFromJsonString(inString, Hot.class);
        Hot hot = hotService.create(pojo);
        if (hot == null) {
            throw new ServiceErrorException("Create hot commodity failed.", MessageKeys.CREATE_HOT_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(hot);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Hot pojo = JsonUtil.getPojoFromJsonString(inString, Hot.class);
        checkHotAvailability(pojo.getId());
        Hot updatedHot = hotService.update(pojo);
        if (updatedHot == null) {
            throw new ServiceErrorException("Update hot commodity failed.", MessageKeys.UPDATE_HOT_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedHot);
    }

    @DELETE
    @Path("entity/{hotid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("hotid") String hotId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkHotAvailability(hotId);
        boolean flag = hotService.delete(hotId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_HOT_SUCCESS, "Delete hot commodity success.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_HOT_FAIL, "Delete hot commodity fail.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("entity/{hotid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHotById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("hotid") String hotId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(hotService.getById(hotId));
    }

    @GET
    @Path("detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHotCommodities(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return JsonUtil.getJsonStringFromPojo(new HotList(hotService.getHotCommodities()));
    }

    @GET
    @Path("admin/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHotCommodityForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(new HotList(hotService.getHotCommodities()));
    }

    private Hot checkHotAvailability(String hotId) {
        Hot existingHot = hotService.getById(hotId);
        if (existingHot == null) {
            String[] param = { hotId };
            throw new ServiceWarningException("Cannot find hot commodity for id " + hotId,
                            MessageKeys.HOT_NOT_FOUND_FOR_ID, param);
        } else {
            return existingHot;
        }
    }
}
