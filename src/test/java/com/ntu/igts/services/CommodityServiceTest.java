package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
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

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        Tag TopLevelTag = new Tag();
        TopLevelTag.setName("top tag");
        TopLevelTag.setStandardName("TOP_TAG" + randomNumber);
        Tag insertedTopLevelTag = tagService.create(TopLevelTag);
        assertNotNull("Create top tag failed", insertedTopLevelTag);

        Tag subTag1 = new Tag();
        subTag1.setName("sub tag1");
        subTag1.setStandardName("SUB_TAG_1" + randomNumber);
        subTag1.setParentId(insertedTopLevelTag.getId());
        Tag insertedSubTag1 = tagService.create(subTag1);
        assertNotNull("Create sub-tag1 failed", insertedSubTag1);

        Tag subTag2 = new Tag();
        subTag2.setName("sub tag2");
        subTag2.setStandardName("SUB_TAG_2" + randomNumber);
        subTag2.setParentId(insertedTopLevelTag.getId());
        Tag insertedSubTag2 = tagService.create(subTag2);
        assertNotNull("Create sub-tag2 failed", insertedSubTag2);

        Commodity testCommodity = new Commodity();
        testCommodity.setTitle("apple");
        testCommodity.setDescription("a good apple");
        testCommodity.setPrice(20.6);
        testCommodity.setCarriage(20);
        testCommodity.setCollectionNumber(5);
        testCommodity.setDistrict("China");
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(insertedTopLevelTag);
        tags.add(insertedSubTag1);
        tags.add(insertedSubTag2);
        testCommodity.setTags(tags);
        Commodity insertedCommodity = commodityService.create(testCommodity);
        assertNotNull("Create item failed", insertedCommodity);

        commodity = insertedCommodity;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        commodity.setDescription("after update");
        Commodity updatedCommodity = commodityService.update(commodity);
        assertNotNull("Update commodity failed", updatedCommodity);
        assertEquals("Update commodity failed", "after update", updatedCommodity.getDescription());
    }

    @Test
    @Order(20)
    public void testGetById() {
        Commodity returnCommodity = commodityService.getById(commodity.getId());
        assertNotNull("Get commodity by id failed", returnCommodity);
        assertEquals("Get commodity by id failed", commodity.getId(), returnCommodity.getId());
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
        query.setSortBy(SortByEnum.COMMODITY_TITILE);
        query.setOrderBy(OrderByEnum.ASC);
        Page<Commodity> commodities = commodityService.getByPage(query);
        assertNotNull("Get commodities by page failed", commodities);
        assertTrue("Get commodities by page failed", commodities.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testGetCommoditiesBySearchTerm() {
        Query query = new Query();
        query.setSearchTerm("apple");
        query.setPage(0);
        query.setSize(2);
        CommodityQueryResult result = commodityService.getCommoditiesBySearchTerm(query);
        assertNotNull("Get commodities failed", result);
        assertTrue("Get commodities failed", result.getContent().size() > 0);
    }

    @Test
    @Order(60)
    public void testDelete() {
        boolean flag = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity failed", flag);
    }
}
