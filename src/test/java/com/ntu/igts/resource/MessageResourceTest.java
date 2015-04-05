package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Message;
import com.ntu.igts.model.Tag;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class MessageResourceTest extends TestBase {

    private static final String BASE_PATH = "message/";

    @Resource
    private CommodityService commodityService;
    @Resource
    private TagService tagService;

    private static Message message;
    private static Commodity commodity;
    private static Tag tag;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpCommodity();

        Message testMessage = new Message();
        testMessage.setCommodityId(commodity.getId());
        testMessage.setContent("test message");
        testMessage.setFloor(1);
        testMessage.setUserId(userId);
        testMessage.setUserName(userName);

        Response createMessageResponse = doPost(BASE_PATH + "entity", userToken,
                        JsonUtil.getJsonStringFromPojo(testMessage));
        assertEquals("Create message failed", Status.OK.getStatusCode(), createMessageResponse.getStatus());

        message = CommonUtil.getEntityFromResponse(createMessageResponse, Message.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateMessageResponse = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(message));
        assertEquals("Update message failed", Status.OK.getStatusCode(), updateMessageResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetMessagesForCommodity() {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        queryParam.put("commodityid", commodity.getId());
        Response response = doGetWithQueryParam(BASE_PATH + "entity", null, queryParam);
        assertEquals("Get messages for commodity failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testDelete() {
        tearDownTag();
        tearDownCommodity();

        String path = BASE_PATH + "entity/" + message.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete message failed", Status.OK.getStatusCode(), response.getStatus());
    }

    private void mockUpCommodity() {
        if (commodity == null) {
            mockUpUser();
            mockUpTag();

            Commodity testCommodity = new Commodity();
            testCommodity.setTitle("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
            testCommodity.setDescription("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
            testCommodity.setPrice(68.6);
            testCommodity.setCarriage(3);
            testCommodity.setCollectionNumber(34);
            testCommodity.setDistrict("浙江衢州");
            testCommodity.setUserId(userId);
            List<Tag> tags = new ArrayList<Tag>();
            tags.add(tag);
            testCommodity.setTags(tags);
            Commodity insertedCommodity = commodityService.create(testCommodity);
            assertNotNull("Create commodity failed", insertedCommodity);

            commodity = insertedCommodity;
        }
    }

    private void mockUpTag() {
        if (tag == null) {
            String randomNumber = UUID.randomUUID().toString().replace("-", "");

            Tag testTag = new Tag();
            testTag.setName("食品");
            testTag.setStandardName("FOOD" + randomNumber);
            Tag insertedTag = tagService.create(testTag);
            assertNotNull("Create tag failed", insertedTag);

            tag = insertedTag;
        }
    }

    private void tearDownTag() {
        if (tag != null) {
            boolean tagDeleteFlag = tagService.delete(tag.getId());
            assertTrue("Delete tag failed", tagDeleteFlag);
        }
    }

    private void tearDownCommodity() {
        if (commodity != null) {
            boolean commodityDeleteFlag = commodityService.delete(commodity.getId());
            assertTrue("Delete commodity failed", commodityDeleteFlag);
        }
    }
}
