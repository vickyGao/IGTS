package com.ntu.igts.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Image;
import com.ntu.igts.model.Slice;
import com.ntu.igts.model.Tag;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.ConfigManagmentUtil;

public class SliceServiceTest extends TestBase {

    @Resource
    private SliceService sliceService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private TagService tagService;
    @Resource
    private ImageService imageService;

    private static Slice slice;
    private static Commodity commodity;
    private static Tag tag;
    private static Image image;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpCommodity();
        mockUpImage();

        Slice testSlice = new Slice();
        testSlice.setCommodityId(commodity.getId());
        testSlice.setDescription("for test");
        testSlice.setDisplaySequence(0);
        testSlice.setImageId(image.getId());
        Slice insertedSlice = sliceService.create(testSlice);
        assertNotNull("Create slice failed", insertedSlice);

        slice = insertedSlice;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        slice.setDescription(slice.getDescription() + " update");
        Slice updatedSlice = sliceService.update(slice);
        assertNotNull("Update slice failed", updatedSlice);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Slice returnSlice = sliceService.getById(slice.getId());
        assertNotNull("Get slice by id failed", returnSlice);
    }

    @Test
    @Order(30)
    public void testGetAll() {
        List<Slice> sliceList = sliceService.getAll();
        assertNotNull("Get all slices failed", sliceList);
        assertTrue("Get all slices failed", sliceList.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean tagDeleteFlag = tagService.delete(tag.getId());
        assertTrue("Delete tag failed", tagDeleteFlag);

        boolean commodityDeleteFlag = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity failed", commodityDeleteFlag);

        boolean imageDeleteFlag = imageService.delete(image.getId());
        assertTrue("Delete image failed", imageDeleteFlag);

        boolean sliceDeleteFlag = sliceService.delete(slice.getId());
        assertTrue("Delete slice failed", sliceDeleteFlag);
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

    private void mockUpImage() {
        if (image == null) {
            Image testImage = new Image();
            testImage.setTitle("test image");
            testImage.setDescription("for test");
            testImage.setUri(ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH) + "/test.jpg");
            Image insertedImage = imageService.create(testImage);
            assertNotNull("Create image failed", insertedImage);

            image = insertedImage;
        }
    }
}
