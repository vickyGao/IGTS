package com.ntu.igts.resource;

import java.io.File;

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
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Image;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.ConfigManagmentUtil;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("image")
public class ImageResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private ImageService imageService;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createImage(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Image pojo = JsonUtil.getPojoFromJsonString(inString, Image.class);
        Image insertedImage = imageService.create(pojo);
        if (insertedImage == null) {
            throw new ServiceErrorException("Create image failed.", MessageKeys.CREATE_IMAGE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(insertedImage);
    }

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateImage(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Image pojo = JsonUtil.getPojoFromJsonString(inString, Image.class);
        Image updatedImage = imageService.update(pojo);
        if (updatedImage == null) {
            throw new ServiceErrorException("Update image failed.", MessageKeys.UPDATE_IMAGE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedImage);
    }

    @DELETE
    @Path("entity/{imageid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteImage(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("imageid") String imageId) {
        filterSessionContext(token, RoleEnum.USER);
        boolean flag = imageService.delete(imageId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_IMAGE_SUCCESS, "Delete image success",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_IMAGE_FAIL, "Delete image fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("location")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStoragePath(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.USER);
        String path = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }
}
