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
import com.ntu.igts.model.Address;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.AddressList;
import com.ntu.igts.services.AddressService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("address")
public class AddressResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private AddressService addressService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Address pojo = JsonUtil.getPojoFromJsonString(inString, Address.class);
        if (StringUtil.isEmpty(pojo.getUserId())) {
            pojo.setUserId(sessionContext.getUserId());
        }
        Address insertedAddress = addressService.create(pojo);
        if (insertedAddress == null) {
            throw new ServiceErrorException("Create address failed.", MessageKeys.CREATE_ADDRESS_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(insertedAddress);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String udpate(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Address pojo = JsonUtil.getPojoFromJsonString(inString, Address.class);
        checkAddressAvailability(pojo.getId());
        Address updatedAddress = addressService.update(pojo);
        if (updatedAddress == null) {
            throw new ServiceErrorException("Edit address failed.", MessageKeys.UPDATE_ADDRESS_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedAddress);
    }

    @DELETE
    @Path("entity/{addressid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("addressid") String addressId) {
        filterSessionContext(token, RoleEnum.USER);
        checkAddressAvailability(addressId);
        boolean flag = addressService.delete(addressId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_ADDRESS_SUCCESS, "Delete address success.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_ADDRESS_FAIL, "Delete address fail.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("entity/{addressid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAddressById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("addressid") String addressId) {
        filterSessionContext(token, RoleEnum.USER);
        return JsonUtil.getJsonStringFromPojo(addressService.getById(addressId));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAddressed(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        return JsonUtil.getJsonStringFromPojo(new AddressList(addressService.getByUserId(sessionContext.getUserId())));
    }

    private Address checkAddressAvailability(String addressId) {
        Address existingAddress = addressService.getById(addressId);
        if (existingAddress == null) {
            String[] param = { addressId };
            throw new ServiceWarningException("Cannot find address for id " + addressId,
                            MessageKeys.ADDRESS_NOT_FOUND_FOR_ID, param);
        } else {
            return existingAddress;
        }
    }
}
