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
        assertNotNull("Create tag failed", insertedTag);

        tag = insertedTag;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        tag.setName("update test tag");
        Response response = doPut(BASE_PATH + "entity", adminToken, JsonUtil.getJsonStringFromPojo(tag));
        assertEquals("Update tag failed", Status.OK.getStatusCode(), response.getStatus());
        String returnJson = response.readEntity(String.class);
        Tag updatedTag = JsonUtil.getPojoFromJsonString(returnJson, Tag.class);
        assertNotNull("Update tag failed", updatedTag);
        assertEquals("Update tag failed", tag.getName(), updatedTag.getName());
    }

    @Test
    @Order(20)
    public void testGetAllTagsWithSubTags() {
        Response response = doGet(BASE_PATH + "entity", null);
        assertEquals("Get all tags with sub-tags failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testGetTagById() {
        Response response = doGet(BASE_PATH + "entity/" + tag.getId(), adminToken);
        assertEquals("Get tag by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(40)
    public void testDelete() {
        Response response = doDelete(BASE_PATH + "entity/" + tag.getId(), adminToken);
        assertEquals("Delete tag failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
