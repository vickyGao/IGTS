package com.ntu.igts.resource;

import java.util.Date;

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
import com.ntu.igts.enums.IndentStatusEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Indent;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.services.IndentService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("indent")
public class IndentResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private IndentService indentService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        if (StringUtil.isEmpty(pojo.getUserId())) {
            pojo.setUserId(sessionContext.getUserId());
        }
        if (StringUtil.isEmpty(pojo.getUserName())) {
            pojo.setUserId(sessionContext.getUserName());
        }
        pojo.setIndentNumber(CommonUtil.getIndentNumber());
        Indent insertedIndent = indentService.create(pojo);
        return JsonUtil.getJsonStringFromPojo(insertedIndent);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        Indent existingIndent = checkIndentAvailability(pojo.getId());
        if (!sessionContext.getUserId().equals(existingIndent.getUserId())) {
            throw new ServiceWarningException("Cannot edit other user's indent",
                            MessageKeys.CANNOT_UPDATE_INDENT_OF_OTHER_USER);
        }
        pojo.setUserId(existingIndent.getUserId());
        pojo.setUserName(existingIndent.getUserName());
        Indent updatedIndent = indentService.update(pojo);
        return JsonUtil.getJsonStringFromPojo(updatedIndent);
    }

    @PUT
    @Path("price")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateIndentPrice(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        Indent existingIndent = checkIndentAvailability(pojo.getId());
        if (!sessionContext.getUserId().equals(existingIndent.getUserId())) {
            throw new ServiceWarningException("Cannot edit other user's indent",
                            MessageKeys.CANNOT_UPDATE_INDENT_OF_OTHER_USER);
        }
        double carriage = pojo.getCarriage();
        double commodityPrice = pojo.getCommodityPrice();
        checkMoneyNumber(carriage, commodityPrice);
        double totalPrice = carriage + commodityPrice;
        existingIndent.setCarriage(carriage);
        existingIndent.setCommodityPrice(commodityPrice);
        existingIndent.setIndentPrice(totalPrice);
        Indent updatedIndent = indentService.update(pojo);
        return JsonUtil.getJsonStringFromPojo(updatedIndent);
    }

    @PUT
    @Path("paytype")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updatePayType(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        Indent existingIndent = checkIndentAvailability(pojo.getId());
        if (!sessionContext.getUserId().equals(existingIndent.getUserId())) {
            throw new ServiceWarningException("Cannot edit other user's indent",
                            MessageKeys.CANNOT_UPDATE_INDENT_OF_OTHER_USER);
        }
        String payType = pojo.getPayType();
        existingIndent.setPayType(payType);
        Indent updatedIndent = indentService.update(pojo);
        return JsonUtil.getJsonStringFromPojo(updatedIndent);
    }

    @PUT
    @Path("status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateIndentStatus(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        IndentStatusEnum indentStatusEnum = IndentStatusEnum.fromValue(pojo.getStatus());
        Indent existingIndent = checkIndentAvailability(pojo.getId());
        if (!sessionContext.getUserId().equals(existingIndent.getUserId())) {
            throw new ServiceWarningException("Cannot edit other user's indent",
                            MessageKeys.CANNOT_UPDATE_INDENT_OF_OTHER_USER);
        }
        Date currentDate = new Date();
        if (IndentStatusEnum.UNPAID.equals(indentStatusEnum)) {
            existingIndent.setIndentTime(currentDate);
        } else if (IndentStatusEnum.PAID.equals(indentStatusEnum)) {
            existingIndent.setPayTime(currentDate);
        } else if (IndentStatusEnum.DELIVERED.equals(indentStatusEnum)) {
            existingIndent.setDeliverTime(currentDate);
        } else if (IndentStatusEnum.COMPLETE.equals(indentStatusEnum)) {
            existingIndent.setDealCompleteTime(currentDate);
        }
        existingIndent.setStatus(messageBuilder.buildMessage(indentStatusEnum.value(),
                        CommonUtil.getLocaleFromRequest(webRequest)));
        Indent updatedIndent = indentService.update(existingIndent);
        return JsonUtil.getJsonStringFromPojo(updatedIndent);
    }

    @DELETE
    @Path("entity/{indentid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteIndent(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("indentid") String indentId) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent existingIndent = checkIndentAvailability(indentId);
        if (!sessionContext.getUserId().equals(existingIndent.getUserId())) {
            throw new ServiceWarningException("Cannot edit other user's indent",
                            MessageKeys.CANNOT_UPDATE_INDENT_OF_OTHER_USER);
        }
        boolean flag = indentService.delete(indentId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_INDENT_SUCCESS, "Delete indent success",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_INDENT_FAIL, "Delete indent fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("entity/{indentid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIndentById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("indentid") String indentId) {
        filterSessionContext(token, RoleEnum.USER);
        return JsonUtil.getJsonStringFromPojo(indentService.getById(indentId));
    }

    @GET
    @Path("entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIndentsForUser(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("page") int currentPage, @QueryParam("size") int pageSize) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Page<Indent> page = indentService.getPaginatedIndentByUserId(currentPage, pageSize, sessionContext.getUserId());
        Pagination<Indent> pagination = new Pagination<Indent>();
        pagination.setContent(page.getContent());
        pagination.setCurrentPage(page.getNumber());
        pagination.setPageCount(page.getTotalPages());
        pagination.setTotalCount(page.getNumberOfElements());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    private Indent checkIndentAvailability(String indentId) {
        Indent existingIndent = indentService.getById(indentId);
        if (existingIndent == null) {
            String[] param = { indentId };
            throw new ServiceWarningException("Cannot find indent for id " + indentId,
                            MessageKeys.INDENT_NOT_FOUND_FOR_ID, param);
        } else {
            return existingIndent;
        }
    }

    private void checkMoneyNumber(double... moneyArray) {
        for (double money : moneyArray) {
            if (!CommonUtil.isLegalMoneyNumber(money)) {
                throw new ServiceWarningException("Not a legal money number", MessageKeys.MONEY_NUMBER_ILLEGAL);
            }
        }
    }
}
