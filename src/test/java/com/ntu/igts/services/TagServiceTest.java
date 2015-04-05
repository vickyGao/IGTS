package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Tag;
import com.ntu.igts.test.Order;

public class TagServiceTest extends TestBase {

    @Resource
    private TagService tagService;
    @Resource
    private CommodityService commodityService;
    private static Tag tag;
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

        tag = TopLevelTag;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        tag.setName("updated top tag");
        Tag updatedTag = tagService.update(tag);
        assertNotNull("Update tag failed", updatedTag);
        assertTrue("Update tag failed", tag.getName().equals(updatedTag.getName()));
    }

    @Test
    @Order(20)
    public void testGetTagsWithSubTagsForParentId() {
        List<Tag> tags = tagService.getTagsWithSubTagsForParentId(tag.getId());
        assertNotNull("Get tags by parentId failed", tags);
        assertTrue("Get tags by parentId failed", tags.size() > 0);
    }

    @Test
    @Order(30)
    public void testGetTopLevelTagWithSubTagsForTagId() {
        Tag returnTag = tagService.getTopLevelTagWithSubTagsForTagId(tag.getId());
        assertNotNull("Get top-level tag with sub-tags failed", returnTag);
        assertTrue("Get top-level tag with sub-tags failed", returnTag.getTags().size() > 0);
    }

    @Test
    @Order(40)
    public void testGetTopLevelTagsForCommodityId() {
        mockUpCommodity();

        List<Tag> topLevelTags = tagService.getTopLevelTagsForCommodityId(commodity.getId());
        assertNotNull("Get top-level tags by commodityId failed", topLevelTags);
        assertTrue("Get top-level tags by commodityId failed", topLevelTags.size() > 0);

    }

    @Test
    @Order(41)
    public void testGetAllTopLevelTags() {
        List<Tag> topLevelTags = tagService.getAllTopLevelTags();
        assertNotNull("Get all top-level tags failed", topLevelTags);
        assertTrue("Get all top-level tags failed", topLevelTags.size() > 0);
    }

    @Test
    @Order(42)
    public void testGetAllTags() {
        List<Tag> returnTags = tagService.getAllTagsWithSubTags();
        assertNotNull("Get all tags failed", returnTags);
        assertTrue("Get all tags failed", returnTags.size() > 0);
    }

    @Test
    @Order(43)
    public void testGetTagsByCommodityId() {
        mockUpCommodity();

        List<Tag> returnTags = tagService.getTagsByCommodityId(commodity.getId());
        assertNotNull("Get tags by commodityId failed", returnTags);
        assertTrue("Get tags by commodityId failed", returnTags.size() > 0);
    }

    @Test
    @Order(44)
    public void testGetTagsHorizontalByCommodityId() {
        mockUpCommodity();
        List<Tag> returnTags = tagService.getTagsHorizontalByCommodityId(commodity.getId());
        assertNotNull("Get tags horizontal by commodityId failed", returnTags);
        assertTrue("Get tags by horizontal commodityId failed", returnTags.size() > 0);
    }

    @Test
    @Order(50)
    public void testDelete() {
        boolean commodityDeleteFlag = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity failed", commodityDeleteFlag);

        List<Tag> subTags = tagService.getTagsWithSubTagsForParentId(tag.getId());
        assertNotNull("Get tags by parentId failed", subTags);
        assertTrue("Get tags by parentId failed", subTags.size() > 0);
        for (Tag subTag : subTags) {
            boolean subTagDeleteFlag = tagService.delete(subTag.getId());
            assertTrue("Delete tag failed", subTagDeleteFlag);
        }
        boolean tagDeleteFlag = tagService.delete(tag.getId());
        assertTrue("Delete tag failed", tagDeleteFlag);
    }

    private void mockUpCommodity() {
        if (commodity == null) {
            Commodity testCommodity = new Commodity();
            testCommodity.setTitle("apple");
            testCommodity.setDescription("a good apple");
            testCommodity.setPrice(20.6);
            testCommodity.setCarriage(20);
            testCommodity.setCollectionNumber(5);
            testCommodity.setDistrict("China");

            List<Tag> tags = new ArrayList<Tag>();
            tags.add(tag);
            testCommodity.setTags(tags);

            Commodity insertedCommodity = commodityService.create(testCommodity);
            assertNotNull("Create commodity failed", insertedCommodity);

            commodity = insertedCommodity;
        }
    }
}
