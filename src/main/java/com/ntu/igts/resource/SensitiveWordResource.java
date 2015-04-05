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
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.services.SensitiveWordService;
import com.ntu.igts.utils.CommonUtil;
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

    @PUT
    @Path("status/{state}/{sensitivewordid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateSensitiveWordState(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("state") ActiveStateEnum activeStateEnum,
                    @PathParam("sensitivewordid") String sensitiveWordId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        SensitiveWord existingSensitiveWord = checkSensitiveWordAvailability(sensitiveWordId);
        existingSensitiveWord.setActiveYN(activeStateEnum.value());
        SensitiveWord updatedSensitiveWord = sensitiveWordService.update(existingSensitiveWord);
        return JsonUtil.getJsonStringFromPojo(updatedSensitiveWord);
    }

    @DELETE
    @Path("entity/{sensitivewordid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("sensitivewordid") String sensitiveWordId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkSensitiveWordAvailability(sensitiveWordId);
        boolean flag = sensitiveWordService.delete(sensitiveWordId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_SENSITIVE_WORD_SUCCESS,
                            "Delete sensitive word success", CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_SENSITIVE_WORD_FAIL, "Delete sensitive word fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("search_term")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBySearchTerm(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @BeanParam Query query) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Page<SensitiveWord> page = sensitiveWordService.getPaginatedSensitiveWord(query);
        Pagination<SensitiveWord> pagination = new Pagination<SensitiveWord>();
        pagination.setContent(page.getContent());
        pagination.setCurrentPage(page.getNumber());
        pagination.setPageCount(page.getTotalPages());
        pagination.setTotalCount(page.getNumberOfElements());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    private SensitiveWord checkSensitiveWordAvailability(String sensitiveWordId) {
        SensitiveWord existingSensitiveWord = sensitiveWordService.getById(sensitiveWordId);
        if (existingSensitiveWord == null) {
            String[] param = { sensitiveWordId };
            throw new ServiceWarningException("Cannot find sensitive word for id " + sensitiveWordId,
                            MessageKeys.SENSITIVE_WORD_NOT_FOUND_FOR_ID, param);
        } else {
            return existingSensitiveWord;
        }
    }
}
