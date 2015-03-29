package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.container.CommodityQueryResult;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.test.Order;

public class CommodityServiceTest extends TestBase {

    @Resource
    private CommodityService commodityService;
    @Resource
    private TagService tagService;

    private static Commodity commodity;
    private static Tag tag;

    @Test
    @Order(0)
    public void testCreate() {
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

    @Test
    @Order(10)
    public void testUpdate() {
        commodity.setDescription(commodity.getDescription() + " update");
        Commodity updatedCommodity = commodityService.update(commodity);
        assertNotNull("Update commodity failed", updatedCommodity);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Commodity returnCommodity = commodityService.getById(commodity.getId());
        assertNotNull("Get commodity by id failed", returnCommodity);
    }

    @Test
    @Order(30)
    public void testGetCommodities() {
        List<Commodity> commodities = commodityService.getCommodities();
        assertNotNull("Get commodities failed", commodities);
        assertTrue("Get commodities failed", commodities.size() > 0);
    }

    @Test
    @Order(40)
    public void testGetByPage() {
        Query query = new Query();
        query.setSearchTerm(commodity.getTitle());
        query.setPage(0);
        query.setSize(5);
        Page<Commodity> commodityPage = commodityService.getByPage(query);
        assertNotNull("Get commodities by page failed", commodityPage);
        assertTrue("Get commodities by page failed", commodityPage.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testGetCommoditiesBySearchTerm() {
        Query query = new Query();
        query.setSearchTerm(commodity.getTitle());
        query.setPage(0);
        query.setSize(5);
        CommodityQueryResult result = commodityService.getCommoditiesBySearchTerm(query);
        assertNotNull("Get commodities by search term failed", result);
        assertTrue("Get commodities by search term failed", result.getContent().size() > 0);
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
