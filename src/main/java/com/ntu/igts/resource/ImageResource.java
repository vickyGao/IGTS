package com.ntu.igts.resource;

import java.io.File;
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

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Image;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.container.ImageList;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.ConfigManagmentUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

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
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Image pojo = JsonUtil.getPojoFromJsonString(inString, Image.class);
        pojo.setUserId(sessionContext.getUserId());
        Image insertedImage = imageService.create(pojo);
        if (insertedImage == null) {
            throw new ServiceErrorException("Create image failed.", MessageKeys.CREATE_IMAGE_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(insertedImage);
    }

    @POST
    @Path("admin/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createImageForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.ADMIN);
        Image pojo = JsonUtil.getPojoFromJsonString(inString, Image.class);
        pojo.setUserId(sessionContext.getUserId());
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
        if (pojo != null) {
            Image existingImage = checkImageAvailability(pojo.getId());
            existingImage.setTitle(pojo.getTitle());
            existingImage.setDescription(pojo.getDescription());
            Image updatedImage = imageService.update(existingImage);
            if (updatedImage == null) {
                throw new ServiceErrorException("Update image failed.", MessageKeys.UPDATE_IMAGE_FAIL);
            }
            return JsonUtil.getJsonStringFromPojo(updatedImage);
        }
        return JsonUtil.getJsonStringFromPojo(pojo);
    }

    @PUT
    @Path("admin/entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateImageForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Image pojo = JsonUtil.getPojoFromJsonString(inString, Image.class);
        if (pojo != null) {
            Image existingImage = checkImageAvailability(pojo.getId());
            existingImage.setTitle(pojo.getTitle());
            existingImage.setDescription(pojo.getDescription());
            Image updatedImage = imageService.update(existingImage);
            if (updatedImage == null) {
                throw new ServiceErrorException("Update image failed.", MessageKeys.UPDATE_IMAGE_FAIL);
            }
            return JsonUtil.getJsonStringFromPojo(updatedImage);
        }
        return JsonUtil.getJsonStringFromPojo(pojo);
    }

    @DELETE
    @Path("entity/{imageid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteImage(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("imageid") String imageId) {
        filterSessionContext(token, RoleEnum.USER);
        checkImageAvailability(imageId);
        boolean flag = imageService.delete(imageId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_IMAGE_SUCCESS, "Delete image success",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_IMAGE_FAIL, "Delete image fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @DELETE
    @Path("admin/entity/{imageid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteImageForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("imageid") String imageId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkImageAvailability(imageId);
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
    @Path("entity/token")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImagesByToken(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        List<Image> images = imageService.getImagesByUserId(sessionContext.getUserId());
        return JsonUtil.getJsonStringFromPojo(new ImageList(images));
    }
    
    @GET
    @Path("admin/entity/token")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImagesByTokenForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.ADMIN);
        List<Image> images = imageService.getImagesByUserId(sessionContext.getUserId());
        return JsonUtil.getJsonStringFromPojo(new ImageList(images));
    }

    @GET
    @Path("location")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStoragePath(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        String path = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    @GET
    @Path("admin/location")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStoragePathForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        String path = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    @GET
    @Path("entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImageByFileName(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("filename") String fileName, @QueryParam("suffix") String suffix) {
        filterSessionContext(token, RoleEnum.USER);
        if (StringUtil.isEmpty(fileName) || StringUtil.isEmpty(suffix)) {
            throw new ServiceWarningException("File name or suffix is required",
                            MessageKeys.FILE_NAME_OR_SUFFIX_IS_REQUIRED);
        }
        String basePath = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        String fileUri = basePath + "/" + fileName.toLowerCase() + "." + suffix.toLowerCase();
        Image returnImage = imageService.getImageByUri(fileUri);
        return JsonUtil.getJsonStringFromPojo(returnImage);
    }

    @GET
    @Path("admin/entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImageByFileNameForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("filename") String fileName, @QueryParam("suffix") String suffix) {
        filterSessionContext(token, RoleEnum.ADMIN);
        if (StringUtil.isEmpty(fileName) || StringUtil.isEmpty(suffix)) {
            throw new ServiceWarningException("File name or suffix is required",
                            MessageKeys.FILE_NAME_OR_SUFFIX_IS_REQUIRED);
        }
        String basePath = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        String fileUri = basePath + "/" + fileName.toLowerCase() + "." + suffix.toLowerCase();
        Image returnImage = imageService.getImageByUri(fileUri);
        return JsonUtil.getJsonStringFromPojo(returnImage);
    }

    @GET
    @Path("entity/{imageid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImageById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("imageid") String imageId) {
        filterSessionContext(token, RoleEnum.USER);
        Image returnImage = imageService.getById(imageId);
        return JsonUtil.getJsonStringFromPojo(returnImage);
    }

    @GET
    @Path("admin/entity/{imageid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getImageByIdForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("imageid") String imageId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Image returnImage = imageService.getById(imageId);
        return JsonUtil.getJsonStringFromPojo(returnImage);
    }

    @GET
    @Path("managedamount")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalManagedImageAmount(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return imageService.getTotalCount();
    }

    @GET
    @Path("managedimages")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllManagedImages(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(new ImageList(imageService.getAll()));
    }

    private Image checkImageAvailability(String imageid) {
        Image existingImage = imageService.getById(imageid);
        if (existingImage == null) {
            String[] param = { imageid };
            throw new ServiceWarningException("Cannot find image for id " + imageid, MessageKeys.HOT_NOT_FOUND_FOR_ID,
                            param);
        } else {
            return existingImage;
        }
    }
}
