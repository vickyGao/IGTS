package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.User;
import com.ntu.igts.model.container.Pagination;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.JsonUtil;

public class UserResourceTest extends TestBase {

    private static final String BASE_PATH = "user/";

    private static User user;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        User testUser = new User();
        testUser.setUserName("user" + randomNumber);
        testUser.setPassword("password");
        Response response = doPost(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(testUser));
        String returnJson = response.readEntity(String.class);
        User insertedUser = JsonUtil.getPojoFromJsonString(returnJson, User.class);
        assertNotNull("Create user fail", insertedUser);

        user = insertedUser;
    }

    @Test
    @Order(10)
    public void testUpdatePassword() {
        user.setPassword("password2");
        Response response = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(user));
        String returnJson = response.readEntity(String.class);
        User updatedUser = JsonUtil.getPojoFromJsonString(returnJson, User.class);
        assertNotNull("Update user password fail", updatedUser);
    }

    @Test
    @Order(20)
    public void testUpdateDetail() {
        user.setAge(20);
        user.setEmail("user@email.com");
        user.setGender("男");
        user.setIdNumber("111111111111111111");
        user.setPhoneNumber("13333333333");
        user.setRealName("张三");
        Response response = doPut(BASE_PATH + "detail", userToken, JsonUtil.getJsonStringFromPojo(user));
        String returnJson = response.readEntity(String.class);
        User updatedUser = JsonUtil.getPojoFromJsonString(returnJson, User.class);
        assertNotNull("Update user detail fail", updatedUser);
    }

    @Test
    @Order(30)
    public void testUpdateUserActiveYnState() {
        ActiveStateEnum activeStateEnum = ActiveStateEnum.ACTIVE;
        String userId = user.getId();
        Response response = doPut(BASE_PATH + "entity/" + activeStateEnum + "/" + userId, adminToken);
        String returnJson = response.readEntity(String.class);
        User updatedUser = JsonUtil.getPojoFromJsonString(returnJson, User.class);
        assertNotNull("Update user active state fail", updatedUser);
    }

    @Test
    @Order(40)
    public void testGetUserByPage() {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("search_term", user.getUserName());
        queryParam.put("search_term", "");
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        queryParam.put("sortby", SortByEnum.USERNAME.value());
        queryParam.put("orderby", OrderByEnum.ASC.value());
        Response response = doGetWithQueryParam(BASE_PATH + "entity/search_term", adminToken, queryParam);
        String returnJson = response.readEntity(String.class);
        @SuppressWarnings("unchecked")
        Pagination<User> pagination = JsonUtil.getPojoFromJsonString(returnJson, Pagination.class);
        assertNotNull("Get users by page fail", pagination);
        assertTrue("Get users by page fail", pagination.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testDelete() {
        Response response = doDelete(BASE_PATH + "entity/" + user.getId(), adminToken);
        String returnJson = response.readEntity(String.class);
        assertNotNull("Delete user fail", returnJson);
    }
}
