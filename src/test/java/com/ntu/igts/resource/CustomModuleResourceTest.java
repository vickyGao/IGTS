package com.ntu.igts.resource;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.CustomModule;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class CustomModuleResourceTest extends TestBase {

    private static final String BASE_PATH = "custommodule/";

    private static CustomModule customModule;

    @Test
    @Order(0)
    public void testCreate() {
        CustomModule testCustomModule = new CustomModule();
        testCustomModule.setDisplayAmount(0);
        testCustomModule.setDisplaySequence(0);
        testCustomModule.setTitle("test custom module");
        testCustomModule.setKeyword("test");

        Response createCustomModuleResponse = doPost(BASE_PATH + "entity", adminToken,
                        JsonUtil.getJsonStringFromPojo(testCustomModule));
        assertEquals("Create custom module failed", Status.OK.getStatusCode(), createCustomModuleResponse.getStatus());

        customModule = CommonUtil.getEntityFromResponse(createCustomModuleResponse, CustomModule.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateCustomModuleResponse = doPut(BASE_PATH + "entity", adminToken,
                        JsonUtil.getJsonStringFromPojo(customModule));
        assertEquals("Update custom module failed", Status.OK.getStatusCode(), updateCustomModuleResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetSliceById() {
        String path = BASE_PATH + "entity/" + customModule.getId();
        Response response = doGet(path, adminToken);
        assertEquals("Get custom module by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testGetCustomModules() {
        Response response = doGet(BASE_PATH + "detail", null);
        assertEquals("Get all custom modules failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(40)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + customModule.getId();
        Response response = doDelete(path, adminToken);
        assertEquals("Delete custom module by id failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
