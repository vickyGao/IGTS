package com.ntu.igts.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
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
import com.ntu.igts.enums.CommodityStatusEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.exception.ServiceErrorException;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageBuilder;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.CommodityList;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.SensitiveWordService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

@Component
@Path("commodity")
public class CommodityResource extends BaseResource {

    @Context
    private HttpServletRequest webRequest;
    @Resource
    private MessageBuilder messageBuilder;
    @Resource
    private CommodityService commodityService;
    @Resource
    private SensitiveWordService sensitiveWordService;
    @Resource
    private TagService tagService;

    /**
     * Create a commodity
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The request body
     * @return The created commodity
     */
    @POST
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createCommodity(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Commodity pojo = JsonUtil.getPojoFromJsonString(inString, Commodity.class);
        if (StringUtil.isEmpty(pojo.getStatus())) {
            pojo.setStatus(CommodityStatusEnum.NORMAL.value());
        }
        pojo.setUserId(sessionContext.getUserId());
        if (!CommonUtil.isLegalMoneyNumber(pojo.getCarriage())) {
            throw new ServiceWarningException("Carriage is not a legal money number",
                            MessageKeys.CARRIAGE_MONEY_NUMBER_ILLEGAL);
        }
        if (!CommonUtil.isLegalMoneyNumber(pojo.getPrice())) {
            throw new ServiceWarningException("Price is not a legal money number",
                            MessageKeys.PRICE_MONEY_NUMBER_ILLEGAL);
        }
        if (sensitiveWordService.containsSensitiveWord(pojo.getTitle())) {
            throw new ServiceWarningException("The commodity title contains sensitive word",
                            MessageKeys.COMMODITY_TITLE_CONTAINS_SENSITIVE_WORD);
        }

        List<Tag> commodityTags = new ArrayList<Tag>();
        List<Tag> tagsPost = pojo.getTags();
        if (tagsPost != null && tagsPost.size() != 0) {
            for (int i = 0; i < tagsPost.size(); i++) {
                Tag commodityTag = tagService.getById(tagsPost.get(i).getId());
                commodityTags.add(commodityTag);
            }
        }

        pojo.setTags(commodityTags);

        pojo.setReleaseDate(new Date());
        Commodity insertedCommodity = commodityService.create(pojo);
        if (insertedCommodity == null) {
            throw new ServiceErrorException("Create commodity failed.", MessageKeys.CREATE_COMMODITY_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(insertedCommodity);
    }

    /**
     * Update the information of a commodity
     * 
     * @param token
     *            The sessionContext id
     * @param inString
     *            The request body
     * @return The updated commodity
     */
    @PUT
    @Path("entity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCommodity(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token, String inString) {
        filterSessionContext(token, RoleEnum.USER);
        Commodity pojo = JsonUtil.getPojoFromJsonString(inString, Commodity.class);
        checkCommodityAvailability(pojo.getId());
        Commodity updatedCommodity = commodityService.update(pojo);
        if (updatedCommodity == null) {
            throw new ServiceErrorException("Update commodity failed.", MessageKeys.UPDATE_COMMODITY_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedCommodity);
    }

    /**
     * User change the active state of the commodity (商品上架、下架)
     * 
     * @param token
     *            The sessionContext id
     * @param requestActiveState
     *            The request active state of the commodity
     * @param commodityId
     *            The id of the commodity
     * @return The updated commodity
     */
    @PUT
    @Path("activestate/{requeststate}/{commodityid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCommodityActiveState(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("requeststate") ActiveStateEnum requestActiveState,
                    @PathParam("commodityid") String commodityId) {
        filterSessionContext(token, RoleEnum.USER);
        Commodity existingCommodity = checkCommodityAvailability(commodityId);
        existingCommodity.setActiveYN(requestActiveState.value());
        Commodity updatedCommodity = commodityService.update(existingCommodity);
        if (updatedCommodity == null) {
            throw new ServiceErrorException("Update commodity failed.", MessageKeys.UPDATE_COMMODITY_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedCommodity);
    }

    @PUT
    @Path("admin/activestate/{requeststate}/{commodityid}")
    public String updateCommodityActiveStateForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("requeststate") ActiveStateEnum requestActiveState,
                    @PathParam("commodityid") String commodityId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Commodity existingCommodity = checkCommodityAvailability(commodityId);
        existingCommodity.setActiveYN(requestActiveState.value());
        Commodity updatedCommodity = commodityService.update(existingCommodity);
        if (updatedCommodity == null) {
            throw new ServiceErrorException("Update commodity failed.", MessageKeys.UPDATE_COMMODITY_FAIL);
        }
        return JsonUtil.getJsonStringFromPojo(updatedCommodity);
    }

    @DELETE
    @Path("entity/{commodityid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteCommodity(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("commodityid") String commodityId) {
        filterSessionContext(token, RoleEnum.USER);
        checkCommodityAvailability(commodityId);
        boolean flag = commodityService.delete(commodityId);
        if (flag) {
            return messageBuilder.buildMessage(MessageKeys.DELETE_COMMODITY_SUCCESS, "Delete commodity success",
                            CommonUtil.getLocaleFromRequest(webRequest));
        } else {
            return messageBuilder.buildMessage(MessageKeys.DELETE_COMMODITY_FAIL, "Delete commodity fail",
                            CommonUtil.getLocaleFromRequest(webRequest));
        }
    }

    @GET
    @Path("admin/detail/{commodityid}")
    public String getCommodityByIdForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("commodityid") String commodityId) {
        filterSessionContext(token, RoleEnum.ADMIN);
        Commodity returnCommodity = commodityService.getCommodityWithDetailById(commodityId);
        returnCommodity.setStatus(messageBuilder.buildMessage(returnCommodity.getStatus(),
                        CommonUtil.getLocaleFromRequest(webRequest)));
        return JsonUtil.getJsonStringFromPojo(returnCommodity);
    }

    /**
     * Get the commodity by id
     * 
     * @param token
     *            The sessionContext id
     * @param commodityId
     *            The id of the commodity
     * @return The commodity get by id
     */
    @GET
    @Path("detail/{commodityid}")
    public String getCommodityById(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @PathParam("commodityid") String commodityId) {
        filterSessionContext(token, RoleEnum.ALL);
        Commodity returnCommodity = commodityService.getCommodityWithDetailById(commodityId);
        returnCommodity.setStatus(messageBuilder.buildMessage(returnCommodity.getStatus(),
                        CommonUtil.getLocaleFromRequest(webRequest)));
        return JsonUtil.getJsonStringFromPojo(returnCommodity);
    }

    /**
     * Get commodities by search parameters with pagination
     * 
     * @param token
     *            The sessionContext id
     * @param query
     *            The search parameters
     * @return The search result of commodities with pagination
     */
    @GET
    @Path("search_term")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCommoditiesBySearchTerm(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @BeanParam Query query) {
        filterSessionContext(token, RoleEnum.ALL);
        CommodityQueryResult commodityQueryResult = commodityService.getCommoditiesBySearchTerm(query);
        List<Commodity> commodities = commodityQueryResult.getContent();
        for (Commodity commodity : commodities) {
            commodity.setStatus(messageBuilder.buildMessage(commodity.getStatus(),
                            CommonUtil.getLocaleFromRequest(webRequest)));
        }
        return JsonUtil.getJsonStringFromPojo(commodityQueryResult);
    }

    @GET
    @Path("admin/search_term")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCommoditiesBySearchTermForAdmin(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @BeanParam Query query) {
        filterSessionContext(token, RoleEnum.ADMIN);
        CommodityQueryResult commodityQueryResult = commodityService.getCommoditiesBySearchTerm(query);
        List<Commodity> commodities = commodityQueryResult.getContent();
        for (Commodity commodity : commodities) {
            commodity.setStatus(messageBuilder.buildMessage(commodity.getStatus(),
                            CommonUtil.getLocaleFromRequest(webRequest)));
        }
        return JsonUtil.getJsonStringFromPojo(commodityQueryResult);
    }

    @GET
    @Path("totalcount")
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalCount(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ALL);
        return commodityService.getTotalCount();
    }

    @GET
    @Path("entity/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCommodities(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token) {
        filterSessionContext(token, RoleEnum.ADMIN);
        List<Commodity> commodities = commodityService.getAll();
        for (Commodity commodity : commodities) {
            commodity.setStatus(messageBuilder.buildMessage(commodity.getStatus(),
                            CommonUtil.getLocaleFromRequest(webRequest)));
        }
        return JsonUtil.getJsonStringFromPojo(new CommodityList(commodities));
    }

    @GET
    @Path("detail")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCommodititesForUser(@HeaderParam(Constants.HEADER_X_AUTH_HEADER) String token,
                    @QueryParam("page") int currentPage, @QueryParam("size") int size,
                    @QueryParam("activestate") ActiveStateEnum activeStateEnum) {
        SessionContext sessionContext = filterSessionContext(token, RoleEnum.USER);
        Page<Commodity> page = commodityService.getCommoditiesForUser(currentPage, size, activeStateEnum,
                        sessionContext.getUserId());
        Pagination<Commodity> pagination = new Pagination<Commodity>();
        pagination.setContent(page.getContent());
        pagination.setCurrentPage(page.getNumber());
        pagination.setPageCount(page.getTotalPages());
        pagination.setTotalCount(page.getNumberOfElements());
        return JsonUtil.getJsonStringFromPojo(pagination);
    }

    private Commodity checkCommodityAvailability(String commodityId) {
        Commodity existingCommodity = commodityService.getById(commodityId);
        if (existingCommodity == null) {
            String[] param = { commodityId };
            throw new ServiceWarningException("Cannot find commodity for id " + commodityId,
                            MessageKeys.COMMODITY_NOT_FOUND_FOR_ID, param);
        } else {
            return existingCommodity;
        }
    }
}
