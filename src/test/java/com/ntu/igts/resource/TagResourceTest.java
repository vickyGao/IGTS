package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.Tag;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.JsonUtil;

public class TagResourceTest extends TestBase {

    private static final String BASE_PATH = "tag/";

    private static Tag tag;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        Tag testTag = new Tag();
        testTag.setName("test tag");
        testTag.setStandardName("TEST_TAG" + randomNumber);

        Response response = doPost(BASE_PATH + "entity", adminToken, JsonUtil.getJsonStringFromPojo(testTag));
        assertEquals("Create tag fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Tag insertedTag = JsonUtil.getPojoFromJsonString(returnJson, Tag.class);
        assertNotNull("Create tag fail", insertedTag);

        tag = insertedTag;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        tag.setName("update test tag");
        Response response = doPut(BASE_PATH + "entity", adminToken, JsonUtil.getJsonStringFromPojo(tag));
        assertEquals("Update tag fail", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Tag updatedTag = JsonUtil.getPojoFromJsonString(returnJson, Tag.class);
        assertNotNull("Update tag fail", updatedTag);
        assertEquals("Update tag fail", tag.getName(), updatedTag.getName());
    }
}
