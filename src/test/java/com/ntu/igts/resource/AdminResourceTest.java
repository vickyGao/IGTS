package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Admin;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class AdminResourceTest extends TestBase {

    private static final String BASE_PATH = "admin/";

    private static Admin admin;

    @Test
    @Order(0)
    public void testCreate() {
        Admin testAdmin = new Admin();
        testAdmin.setAdminName("admin" + CommonUtil.getRandomNumber());
        testAdmin.setAdminPassword("password");

        Response createAdminResponse = doPost(BASE_PATH + "entity", adminToken,
                        JsonUtil.getJsonStringFromPojo(testAdmin));
        assertEquals("Create admin failed", Status.OK.getStatusCode(), createAdminResponse.getStatus());

        admin = CommonUtil.getEntityFromResponse(createAdminResponse, Admin.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateAdminResponse = doPut(BASE_PATH + "entity", adminToken, JsonUtil.getJsonStringFromPojo(admin));
        assertEquals("Update admin failed", Status.OK.getStatusCode(), updateAdminResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetAdminById() {
        String path = BASE_PATH + "detail/" + admin.getId();
        Response response = doGet(path, adminToken);
        assertEquals("Get admin by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testGetPaginatedAdmins() {
        String path = BASE_PATH + "search_term";
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("search_term", admin.getAdminName());
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        queryParam.put("sortby", SortByEnum.ADMIN_NAME.name());
        queryParam.put("orderby", OrderByEnum.ASC.name());
        Response response = doGetWithQueryParam(path, adminToken, queryParam);
        assertEquals("Get paginated admins failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(31)
    public void testGetAdminByToken() {
        Response response = doGet(BASE_PATH + "detail/token", adminToken);
        assertEquals("Get admin by token fail", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(40)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + admin.getId();
        Response response = doDelete(path, adminToken);
        assertEquals("Delete admin failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
