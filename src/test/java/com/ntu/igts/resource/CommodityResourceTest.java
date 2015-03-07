package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.JsonUtil;
import com.ntu.igts.utils.StringUtil;

public class CommodityResourceTest extends TestBase {

    private static final String BASE_PATH = "commodity/";

    private static Commodity commodity;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        Tag tag = new Tag();
        tag.setName("零食-test");
        tag.setStandardName("FOOD-" + randomNumber);

        Response createTagResponse = doPost("tag/entity", adminToken, JsonUtil.getJsonStringFromPojo(tag));
        assertEquals("Create tag fail", Status.OK.getStatusCode(), createTagResponse.getStatus());
        String returnTagJson = createTagResponse.readEntity(String.class);
        Tag insertedTag = JsonUtil.getPojoFromJsonString(returnTagJson, Tag.class);

        Commodity testCommodity = new Commodity();
        testCommodity.setTitle("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
        testCommodity.setDescription("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
        testCommodity.setPrice(68.6);
        testCommodity.setCarriage(3);
        testCommodity.setCollectionNumber(34);
        testCommodity.setDistrict("浙江衢州");
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(insertedTag);
        testCommodity.setTags(tags);

        Response createCommodityResponse = doPost(BASE_PATH + "entity", userToken,
                        JsonUtil.getJsonStringFromPojo(testCommodity));
        assertEquals("Create commodity fail", Status.OK.getStatusCode(), createCommodityResponse.getStatus());
        String returnCommodityJson = createCommodityResponse.readEntity(String.class);
        Commodity insertedCommodity = JsonUtil.getPojoFromJsonString(returnCommodityJson, Commodity.class);
        assertNotNull("Create commodity fail", insertedCommodity);

        commodity = insertedCommodity;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        commodity.setDescription("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食-update");
        Response response = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(commodity));
        assertEquals("Update commodity fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Commodity updatedCommodity = JsonUtil.getPojoFromJsonString(returnJson, Commodity.class);
        assertNotNull("Update commodity fail", updatedCommodity);
    }

    @Test
    @Order(20)
    public void testUpdateActiveState() {
        String path = BASE_PATH + "activestate/" + ActiveStateEnum.ACTIVE.name() + "/" + commodity.getId();
        Response response = doPut(path, userToken);
        assertEquals("Update commodity active state fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Commodity updatedCommodity = JsonUtil.getPojoFromJsonString(returnJson, Commodity.class);
        assertNotNull("Update commodity active state fail", updatedCommodity);
    }

    @Test
    @Order(30)
    public void testGetById() {
        String path = BASE_PATH + "entity/" + commodity.getId();
        Response response = doGet(path, userToken);
        assertEquals("Get commodity by id fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Commodity returnCommodity = JsonUtil.getPojoFromJsonString(returnJson, Commodity.class);
        assertNotNull("Get commodity by id fail", returnCommodity);
    }

    @Test
    @Order(40)
    public void testGetBySearchTerm() {
        String path = BASE_PATH + "search_term";
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("search_term", "巧克力");
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        queryParam.put("sortby", SortByEnum.COMMODITY_TITILE.name());
        Response response = doGetWithQueryParam(path, StringUtil.EMPTY, queryParam);
        assertEquals("Get commodities by search term fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        CommodityQueryResult commodityQueryResult = JsonUtil.getPojoFromJsonString(returnJson,
                        CommodityQueryResult.class);
        assertNotNull("Get commodities by search term fail", commodityQueryResult);
        assertTrue("Get commodities by search term fail", commodityQueryResult.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + commodity.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete commodity fail", Status.OK.getStatusCode(), response.getStatus());
    }
}
