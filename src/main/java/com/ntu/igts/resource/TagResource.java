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
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.TagList;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("tag")
public class TagResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private TagService tagService;
    @Resource
    private MessageBuilder messageBuilder;

    /**
     * Create a new tag
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The post body of tag pojo
     * @return The created tag
     */
    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Tag pojo = JsonUtil.getPojoFromJsonString(inString, Tag.class);
        Tag sameStandardNameTag = tagService.getTagForStandardName(pojo.getStandardName());
        if (sameStandardNameTag != null) {
            throw new ServiceWarningException("Cannot create tag with an existing standard name.",
                            MessageKeys.CANNOT_CREATE_TAG_WITH_EXISTING_STANDARD_NAME);
        }
        Tag insertedTag = tagService.create(pojo);
        if (insertedTag == null) {
            String[] param = { pojo.getName() };
            throw new ServiceErrorException("Create tag " + pojo.getName() + " failed.", MessageKeys.CREATE_TAG_FAILED,
                            param);
        }
        return JsonUtil.getJsonStringFromPojo(insertedTag);
    }

    /**
     * Update a tag
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The put body of tag pojo
     * @return The updated tag
     */
    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Tag pojo = JsonUtil.getPojoFromJsonString(inString, Tag.class);
        checkTagAvailability(pojo.getId());
        Tag updatedTag = tagService.update(pojo);
        if (updatedTag == null) {
            String[] param = { pojo.getName() };
            throw new ServiceErrorException("Update tag " + pojo.getName() + " failed", MessageKeys.UPDATE_TAG_FAILED,
                            param);
        }
        return JsonUtil.getJsonStringFromPojo(updatedTag);
    }

    @DELETE
    @Path("entity/{tagid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("tagid") String tagId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        checkTagAvailability(tagId);
        boolean flag = tagService.delete(tagId);
        String[] param = { tagId };
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_TAG_FOR_ID_SUCCESS, param, "Delete tag " + tagId
                            + " success.", CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_TAG_FOR_ID_FAIL, param, "Delete tag " + tagId
                            + " fail.", CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    /**
     * Get all tags with sub-tags (Used for display all tags in UI)
     * 
     * @return All tags with sub-tags
     */
    @GET
    @Path("detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTagsWithSubTags(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return JsonUtil.getJsonStringFromPojo(new TagList(tagService.getAllTagsWithSubTags()));
    }

    @GET
    @Path("entity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTopLevelTags(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(new TagList(tagService.getAllTopLevelTags()));
    }

    @GET
    @Path("entity/{tagid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTagById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, @PathParam("tagid") String tagId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return JsonUtil.getJsonStringFromPojo(tagService.getById(tagId));
    }

    @GET
    @Path("totalcount")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalCount(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ADMIN);
        return tagService.getTotalCount();
    }

    private Tag checkTagAvailability(String tagId) {
        Tag existingTag = tagService.getById(tagId);
        if (existingTag == null) {
            String[] param = { tagId };
            throw new ServiceWarningException("Cannot find tag for id " + tagId, MessageKeys.TAG_NOT_FOUND_FOR_ID,
                            param);
        } else {
            return existingTag;
        }
    }
}
