package com.ntu.igts.resource;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.Image;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class ImageResourceTest extends TestBase {

    private static final String BASE_PATH = "image/";

    private static Image image;

    @Test
    @Order(0)
    public void testCreate() {
        Image testImage = new Image();
        testImage.setTitle("test image");
        testImage.setDescription("for test");

        Response createImageResponse = doPost(BASE_PATH + "entity", userToken,
                        JsonUtil.getJsonStringFromPojo(testImage));
        assertEquals("Create image failed", Status.OK.getStatusCode(), createImageResponse.getStatus());

        image = CommonUtil.getEntityFromResponse(createImageResponse, Image.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateImageResponse = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(image));
        assertEquals("Update image failed", Status.OK.getStatusCode(), updateImageResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetStoragePath() {
        String path = BASE_PATH + "location";
        Response response = doGet(path, userToken);
        assertEquals("Get image storage path failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + image.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete image failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
