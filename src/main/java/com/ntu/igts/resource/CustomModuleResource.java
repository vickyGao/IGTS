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
import com.ntu.igts.model.CustomModule;
import com.ntu.igts.model.container.CustomModuleList;
import com.ntu.igts.services.CustomModuleService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("custommodule")
public class CustomModuleResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private CustomModuleService customModuleService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        CustomModule pojo = JsonUtil.getPojoFromJsonString(inString, CustomModule.class);
        CustomModule insertedCustomModule = customModuleService.create(pojo);
        if (insertedCustomModule == null) {
            throw new ServiceErrorException("Create custom module failed.", MessageKeys.CREATE_CUSTOMMODULE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(insertedCustomModule);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String udpate(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        CustomModule pojo = JsonUtil.getPojoFromJsonString(inString, CustomModule.class);
        checkCustomModuleAvailability(pojo.getId());
        CustomModule updatedCustomModule = customModuleService.update(pojo);
        if (updatedCustomModule == null) {
            throw new ServiceErrorException("Edit custom module failed.", MessageKeys.UPDATE_CUSTOMMODULET_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedCustomModule);
    }

    @DELETE
    @Path("entity/{custommoduleid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("custommoduleid") String customModuleId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkCustomModuleAvailability(customModuleId);
        boolean flag = customModuleService.delete(customModuleId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_CUSTOMMODULE_SUCCESS,
                            "Delete custom module success.", CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_CUSTOMMODULET_FAIL, "Delete custom module fail.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("entity/{custommoduleid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSliceById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("custommoduleid") String customModuleId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(customModuleService.getById(customModuleId));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomModules(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return JsonUtil.getJsonStringFromPojo(new CustomModuleList(customModuleService.getCustomModules()));
    }

    private CustomModule checkCustomModuleAvailability(String customModuleId) {
        CustomModule existingCustomModule = customModuleService.getById(customModuleId);
        if (existingCustomModule == null) {
            String[] param = { customModuleId };
            throw new ServiceWarningException("Cannot find custom module for id " + customModuleId,
                            MessageKeys.CUSTOMMODULE_NOT_FOUND_FOR_ID, param);
        } else {
            return existingCustomModule;
        }
    }
}
