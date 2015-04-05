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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Message;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.services.MessageService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("message")
public class MessageResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private MessageService messageService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Message pojo = JsonUtil.getPojoFromJsonString(inString, Message.class);
        if (StringUtil.isEmpty(pojo.getUserId())) {
            pojo.setUserId(sessionContext.getUserId());
        }
        if (StringUtil.isEmpty(pojo.getUserName())) {
            pojo.setUserId(sessionContext.getUserName());
        }
        Message insertedMessage = messageService.create(pojo);
        return JsonUtil.getJsonStringFromPojo(insertedMessage);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Message pojo = JsonUtil.getPojoFromJsonString(inString, Message.class);
        checkMessageAvailability(pojo.getId());
        Message updatedMessage = messageService.update(pojo);
        return JsonUtil.getJsonStringFromPojo(updatedMessage);
    }

    @DELETE
    @Path("entity/{messageid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("messageid") String messageId) {
        filterSessionContext(token, RoleEnum.USER);
        checkMessageAvailability(messageId);
        boolean flag = messageService.delete(messageId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_MESSAGE_SUCCESS, "Delete message success",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_MESSAGE_FAIL, "Delete message fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessages(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("page") int currentPage, @QueryParam("size") int pageSize,
                    @QueryParam("commodityid") String commodityId) {
        Page<Message> page = messageService.getPaginatedMessagesByCommodity(currentPage, pageSize, commodityId);
        Pagination<Message> pagination = new Pagination<Message>();
        pagination.setContent(page.getContent());
        pagination.setCurrentPage(page.getNumber());
        pagination.setPageCount(page.getTotalPages());
        pagination.setTotalCount(page.getNumberOfElements());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    private Message checkMessageAvailability(String messageId) {
        Message existingMessage = messageService.getById(messageId);
        if (existingMessage == null) {
            String[] param = { messageId };
            throw new ServiceWarningException("Cannot find message for id " + messageId,
                            MessageKeys.MESSAGE_NOT_FOUND_FOR_ID, param);
        } else {
            return existingMessage;
        }
    }
}
