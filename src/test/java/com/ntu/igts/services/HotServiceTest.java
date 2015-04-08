package com.ntu.igts.services;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Hot;
import com.ntu.igts.test.Order;

public class HotServiceTest extends TestBase {

    @Resource
    private HotService hotService;
    private static Hot hot;

    @Test
    @Order(0)
    public void testCreate() {
        Hot testHot = new Hot();
        testHot.setCommodityId(UUID.randomUUID().toString());
        testHot.setDescription("test hot commodity");
        testHot.setDisplaySequence(0);
        testHot.setImageId(UUID.randomUUID().toString());
        Hot insertedHot = hotService.create(testHot);
        assertNotNull("Create hot commodity failed", insertedHot);

        hot = insertedHot;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        hot.setDescription(hot.getDescription() + " update");
        Hot updatedHot = hotService.update(hot);
        assertNotNull("Update hot commodity failed", updatedHot);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Hot returnHot = hotService.getById(hot.getId());
        assertNotNull("Get hot commodity by id failed", returnHot);
    }

    @Test
    @Order(30)
    public void testGetHotCommodities() {
        List<Hot> hotCommodityList = hotService.getHotCommodities();
        assertNotNull("Get hot commodities failed", hotCommodityList);
        assertTrue("Get hot commodities failed", hotCommodityList.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean hotDeleteFlag = hotService.delete(hot.getId());
        assertTrue("Delete hot commodity failed", hotDeleteFlag);
    }
}
