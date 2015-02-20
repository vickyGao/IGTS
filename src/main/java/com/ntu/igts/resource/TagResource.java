package com.ntu.igts.resource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Tag;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.JsonUtil;

@Component
@Path("tag")
public class TagResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private TagService tagService;
    @Resource
    private MessageBuilder messageBuilder;

    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
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

    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        Tag pojo = JsonUtil.getPojoFromJsonString(inString, Tag.class);
        Tag existingTag = tagService.getById(pojo.getId());
        if (existingTag == null) {
            String[] param = { pojo.getId() };
            throw new ServiceWarningException("Cannot find tag for id " + pojo.getId(),
                            MessageKeys.TAG_NOT_FOUND_FOR_ID, param);

        }
        Tag updatedTag = tagService.update(pojo);
        if (updatedTag == null) {
            String[] param = { pojo.getName() };
            throw new ServiceErrorException("Update tag " + pojo.getName() + " failed", MessageKeys.UPDATE_TAG_FAILED,
                            param);
        }
        return JsonUtil.getJsonStringFromPojo(updatedTag);
    }
}
