package com.ntu.igts.resource;

import java.util.Date;
import java.util.List;

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
import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.IndentStatusEnum;
import com.ntu.igts.enums.PayTypeEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Indent;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.IndentService;
import com.ntu.igts.services.UserService;
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
    @Resource
    private CommodityService commodityService;
    @Resource
    private UserService userService;

    /**
     * Create Indent, necessary fields are (indentaddress, phonenumber, indentmessage)
     * 
     * @param token
     *            The x-auth-token
     * @param commodityId
     *            The id of Commodity
     * @param inString
     *            The post body
     * @return The created Indent
     */
    @POST
    @Path("entity/{commodityId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("commodityId") String commodityId, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent pojo = JsonUtil.getPojoFromJsonString(inString, Indent.class);
        Commodity commodity = commodityService.getById(commodityId);
        if (commodity == null) {
            String[] param = { commodityId };
            throw new ServiceWarningException("Cannot find commodity for id " + commodityId,
                            MessageKeys.COMMODITY_NOT_FOUND_FOR_ID, param);
        }
        if (ActiveStateEnum.NEGATIVE.value().equals(commodity.getActiveYN())) {
            throw new ServiceWarningException("Cannot buy a under carriage commodity",
                            MessageKeys.CANNOT_BUY_UNDER_CARRIAGE_COMMODITY);
        }

        // Upon a user has bought a commodity, set activeYN as N
        commodity.setActiveYN(ActiveStateEnum.NEGATIVE.value());
        commodityService.update(commodity);

        // Start create the Indent
        if (StringUtil.isEmpty(pojo.getUserId())) {
            pojo.setUserId(sessionContext.getUserId());
        }
        if (StringUtil.isEmpty(pojo.getUserName())) {
            pojo.setUserName(sessionContext.getUserName());
        }
        if (StringUtil.isEmpty(pojo.getSellerId())) {
            pojo.setSellerId(commodity.getUserId());
        }
        pojo.setCarriage(commodity.getCarriage());
        pojo.setCommodityId(commodityId);
        pojo.setCommodityPrice(commodity.getPrice());
        pojo.setIndentTime(new Date());
        pojo.setIndentPrice(pojo.getCarriage() + pojo.getCommodityPrice());
        pojo.setStatus(IndentStatusEnum.UNPAID.value());
        pojo.setIndentNumber(CommonUtil.getIndentNumber());
        Indent insertedIndent = indentService.create(pojo);
        return JsonUtil.getJsonStringFromPojo(insertedIndent);
    }

    /**
     * Update the indent's status, if the operation is to update the status to PAID, will set pay type together
     * 
     * @param token
     *            The x-auth-token
     * @param inString
     *            The put body
     * @return The updated Indent
     */
    @PUT
    @Path("entity/{status}/{indentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("status") IndentStatusEnum statusEnum, @PathParam("indentId") String indentId,
                    @HeaderParam("paytype") PayTypeEnum payTypeEnum) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Indent existingIndent = checkIndentAvailability(indentId);

        // Change Indent from un-paid to paid, will lock parts of user's money
        if (IndentStatusEnum.PAID.equals(statusEnum)) {
            indentService.purchase(existingIndent, sessionContext.getUserId(), payTypeEnum);
        } else if (IndentStatusEnum.DELIVERED.equals(statusEnum)) {
            if (!IndentStatusEnum.PAID.value().equals(existingIndent.getStatus())) {
                throw new ServiceWarningException("Cannot deliver the goods as buyer has not paid",
                                MessageKeys.CANNOT_DELIVER_GOODS_NOT_PAID);
            }
            existingIndent.setDeliverTime(new Date());
            // Change Indent from delivered/returning to complete
        } else if (IndentStatusEnum.COMPLETE.equals(statusEnum)) {
            if (IndentStatusEnum.DELIVERED.value().equals(existingIndent.getStatus())) {
                indentService.dealComplete(existingIndent, sessionContext.getUserId());
            } else if (IndentStatusEnum.RETURNING.value().equals(existingIndent.getStatus())) {
                indentService.returnComplete(existingIndent, sessionContext.getUserId());
            }
            // Change from up-paid to cancelled
        } else if (IndentStatusEnum.CANCELLED.equals(statusEnum)) {
            indentService.cancelDeal(existingIndent, sessionContext.getUserId());
            // Change from paid/delivered to returning
        } else if (IndentStatusEnum.RETURNING.equals(statusEnum)) {
            indentService.returnDeal(existingIndent, sessionContext.getUserId());
        } else {
            String[] param = { statusEnum.value() };
            String statusLocale = messageBuilder.buildMessage(statusEnum.value(),
                            CommonUtil.getLocaleFromRequest(webRequest));
            throw new ServiceWarningException("The status " + statusLocale + " is not supported to be updated",
                            MessageKeys.STATUS_NOT_SUPPORTED, param);
        }
        existingIndent.setStatus(statusEnum.value());

        Indent updatedIndent = indentService.update(existingIndent);
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
        Indent returnIndent = indentService.getById(indentId);
        returnIndent.setStatus(messageBuilder.buildMessage(returnIndent.getStatus(),
                        CommonUtil.getLocaleFromRequest(webRequest)));
        returnIndent.setCommodity(commodityService.getById(returnIndent.getCommodityId()));
        return JsonUtil.getJsonStringFromPojo(returnIndent);
    }

    @GET
    @Path("entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIndentsForBuyer(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("page") int currentPage, @QueryParam("size") int pageSize) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Page<Indent> page = indentService
                        .getPaginatedIndentByBuyerId(currentPage, pageSize, sessionContext.getUserId());
        Pagination<Indent> pagination = new Pagination<Indent>();
        List<Indent> indents = page.getContent();
        for (Indent indent : indents) {
            indent.setStatus(messageBuilder.buildMessage(indent.getStatus(),
                            CommonUtil.getLocaleFromRequest(webRequest)));
            indent.setCommodity(commodityService.getById(indent.getCommodityId()));
        }
        pagination.setContent(indents);
        pagination.setCurrentPage(page.getNumber());
        pagination.setPageCount(page.getTotalPages());
        pagination.setTotalCount(page.getNumberOfElements());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    @GET
    @Path("entity/seller")
    @Produces(MediaType.APPLICATION_JSON)
    public String getIndentsForSeller(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("page") int currentPage, @QueryParam("size") int pageSize) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Page<Indent> page = indentService.getPaginatedIndentBySellerId(currentPage, pageSize,
                        sessionContext.getUserId());
        Pagination<Indent> pagination = new Pagination<Indent>();
        List<Indent> indents = page.getContent();
        for (Indent indent : indents) {
            indent.setStatus(messageBuilder.buildMessage(indent.getStatus(),
                            CommonUtil.getLocaleFromRequest(webRequest)));
            indent.setCommodity(commodityService.getById(indent.getCommodityId()));
        }
        pagination.setContent(indents);
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
