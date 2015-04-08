package com.ntu.igts.services;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Cover;
import com.ntu.igts.test.Order;

public class CoverServiceTest extends TestBase {

    @Resource
    private CoverService coverService;
    @Resource
    private CommodityService commodityService;
    private static Cover mainCover;
    private static Cover secondCover;
    public static Commodity commodity;

    @Test
    @Order(0)
    public void testCreate() {
        Commodity testCommodity = new Commodity();
        testCommodity.setTitle("test commodity");
        Commodity insertedCommodity = commodityService.create(testCommodity);
        assertNotNull("Create commodity fail", insertedCommodity);
        commodity = testCommodity;

        Cover cover1 = new Cover();
        cover1.setCommodityId(insertedCommodity.getId());
        cover1.setMainCoverYN("Y");
        cover1.setDisplaySequence(0);
        cover1.setDescription("main cover");
        Cover insertedCover1 = coverService.create(cover1);
        assertNotNull("Create cover fail", insertedCover1);
        mainCover = cover1;

        Cover cover2 = new Cover();
        cover2.setCommodityId(insertedCommodity.getId());
        cover2.setMainCoverYN("N");
        cover2.setDisplaySequence(1);
        cover2.setDescription("back image");
        Cover insertedCover2 = coverService.create(cover2);
        assertNotNull("Create cover fail", insertedCover2);
        secondCover = cover2;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        secondCover.setDescription("back image update");
        Cover updatedCover = coverService.update(secondCover);
        assertNotNull("Update cover fail", updatedCover);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Cover returnCover = coverService.getById(mainCover.getId());
        assertNotNull("Get cover by id fail", returnCover);
    }

    @Test
    @Order(30)
    public void testGetCoversByCommodityId() {
        List<Cover> covers = coverService.getCoversByCommodityId(commodity.getId());
        assertNotNull("Get covers by commodity id fail", covers);
        assertTrue("Get covers by commodity id fail", covers.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean flag1 = coverService.delete(mainCover.getId());
        assertTrue("Delete cover fail", flag1);
        boolean flag2 = coverService.delete(secondCover.getId());
        assertTrue("Delete cover fail", flag2);
        boolean flag3 = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity fail", flag3);

    }
}
