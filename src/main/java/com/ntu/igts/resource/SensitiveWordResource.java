package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.services.SensitiveWordService;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("sensitiveword")
public class SensitiveWordResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private SensitiveWordService sensitiveWordService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        SensitiveWord pojo = JsonUtil.getPojoFromJsonString(inString, SensitiveWord.class);
        SensitiveWord insertedSensitiveWord = sensitiveWordService.create(pojo);
        return JsonUtil.getJsonStringFromPojo(insertedSensitiveWord);
    }
}
