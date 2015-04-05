package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Image;
import com.ntu.igts.model.Slice;
import com.ntu.igts.model.Tag;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.ConfigManagmentUtil;
import com.ntu.igts.utils.JsonUtil;

public class SliceResourceTest extends TestBase {

    private static final String BASE_PATH = "slice/";

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

        Response createSliceResponse = doPost(BASE_PATH + "entity", adminToken,
                        JsonUtil.getJsonStringFromPojo(testSlice));
        assertEquals("Create slice failed", Status.OK.getStatusCode(), createSliceResponse.getStatus());

        slice = CommonUtil.getEntityFromResponse(createSliceResponse, Slice.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateSliceResponse = doPut(BASE_PATH + "entity", adminToken, JsonUtil.getJsonStringFromPojo(slice));
        assertEquals("Update slice failed", Status.OK.getStatusCode(), updateSliceResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetSliceById() {
        String path = BASE_PATH + "entity/" + slice.getId();
        Response response = doGet(path, adminToken);
        assertEquals("Get slice by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testGetSlices() {
        Response response = doGet(BASE_PATH + "entity", null);
        assertEquals("Get all slices failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(40)
    public void testDelete() {
        tearDownTag();
        tearDownCommodity();
        tearDownImage();

        String path = BASE_PATH + "entity/" + slice.getId();
        Response response = doDelete(path, adminToken);
        assertEquals("Delete slice failed", Status.OK.getStatusCode(), response.getStatus());
    }

    private void mockUpCommodity() {
        if (commodity == null) {
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

    private void tearDownImage() {
        if (image != null) {
            boolean imageDeleteFlag = imageService.delete(image.getId());
            assertTrue("Delete image failed", imageDeleteFlag);
        }
    }
}
