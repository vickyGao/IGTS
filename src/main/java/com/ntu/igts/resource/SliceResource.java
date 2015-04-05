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
import com.ntu.igts.model.Slice;
import com.ntu.igts.model.container.SliceList;
import com.ntu.igts.services.SliceService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("slice")
public class SliceResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private SliceService sliceService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Slice pojo = JsonUtil.getPojoFromJsonString(inString, Slice.class);
        Slice slice = sliceService.create(pojo);
        if (slice == null) {
            throw new ServiceErrorException("Create slice failed.", MessageKeys.CREATE_SLICE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(slice);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String udpate(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Slice pojo = JsonUtil.getPojoFromJsonString(inString, Slice.class);
        checkSliceAvailability(pojo.getId());
        Slice slice = sliceService.update(pojo);
        if (slice == null) {
            throw new ServiceErrorException("Update slice failed.", MessageKeys.UPDATE_SLICE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(slice);
    }

    @DELETE
    @Path("entity/{sliceid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("sliceid") String sliceId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkSliceAvailability(sliceId);
        boolean flag = sliceService.delete(sliceId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_SLICE_SUCCESS, "Delete slice success.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_SLICE_FAIL, "Delete slice fail.",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("entity/{sliceid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSliceById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("sliceid") String sliceId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(sliceService.getById(sliceId));
    }

    @GET
    @Path("entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSlices(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return JsonUtil.getJsonStringFromPojo(new SliceList(sliceService.getAll()));
    }

    private Slice checkSliceAvailability(String sliceId) {
        Slice existingSlice = sliceService.getById(sliceId);
        if (existingSlice == null) {
            String[] param = { sliceId };
            throw new ServiceWarningException("Cannot find slice for id " + sliceId,
                            MessageKeys.SLICE_NOT_FOUND_FOR_ID, param);
        } else {
            return existingSlice;
        }
    }
}
