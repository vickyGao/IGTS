package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Message;
import com.ntu.igts.model.Tag;
import com.ntu.igts.test.Order;

public class MessageServiceTest extends TestBase {

    @Resource
    private MessageService messageService;
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
        mockUpUser();
        mockUpCommodity();

        Message testMessage = new Message();
        testMessage.setCommodityId(commodity.getId());
        testMessage.setContent("test message");
        testMessage.setFloor(1);
        testMessage.setUserId(user.getId());
        testMessage.setUserName(user.getUserName());
        Message insertedMessage = messageService.create(testMessage);
        assertNotNull("Create message failed", insertedMessage);

        message = insertedMessage;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        message.setContent(message.getContent() + " udpate");
        Message updatedMessage = messageService.update(message);
        assertNotNull("Update message failed", updatedMessage);
    }

    @Test
    @Order(20)
    public void testgetById() {
        Message returnMessage = messageService.getById(message.getId());
        assertNotNull("Get message by id failed", returnMessage);
    }

    @Test
    @Order(30)
    public void testGetByCommodityId() {
        List<Message> messages = messageService.getByCommodityId(commodity.getId());
        assertNotNull("Get messages by commodity id failed", messages);
        assertTrue("Get messages by commodity id failed", messages.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean tagDeleteFlag = tagService.delete(tag.getId());
        assertTrue("Delete tag failed", tagDeleteFlag);

        boolean userDeleteFlag = userService.delete(user.getId());
        assertTrue("Delete user failed", userDeleteFlag);

        boolean commodityDeleteFlag = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity failed", commodityDeleteFlag);

        boolean messageDeleteFlag = messageService.delete(message.getId());
        assertTrue("Delete message failed", messageDeleteFlag);
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
            testCommodity.setUserId(user.getId());
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
}
