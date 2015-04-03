package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Bill;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.BillList;
import com.ntu.igts.services.BillService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("bill")
public class BillResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private BillService billService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Bill pojo = JsonUtil.getPojoFromJsonString(inString, Bill.class);
        if (StringUtil.isEmpty(pojo.getUserId())) {
            pojo.setUserId(sessionContext.getUserId());
        }
        Bill insertedBill = billService.create(pojo);
        return JsonUtil.getJsonStringFromPojo(insertedBill);
    }

    @DELETE
    @Path("entity/{billid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("billid") String billId) {
        checkBillAvailability(billId);
        boolean flag = billService.delete(billId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_BILL_SUCCESS, "Delete bill success.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_BILL_FAIL, "Delete bill fail.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBills(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        return JsonUtil.getJsonStringFromPojo(new BillList(billService.getByUserId(sessionContext.getUserId())));
    }

    private Bill checkBillAvailability(String billId) {
        Bill exisingBill = billService.getById(billId);
        if (exisingBill == null) {
            String[] param = { billId };
            throw new ServiceWarningException("Cannot find bill for id " + billId, MessageKeys.BILL_NOT_FOUND_FOR_ID,
                            param);
        } else {
            return exisingBill;
        }
    }
}
