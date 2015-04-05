package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.enums.IndentStatusEnum;
import com.ntu.igts.model.Indent;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class IndentResourceTest extends TestBase {

    private static final String BASE_PATH = "indent/";

    private static Indent indent;

    @Test
    @Order(0)
    public void testCreate() {
        Indent testIndent = new Indent();
        testIndent.setIndentNumber(CommonUtil.getIndentNumber());
        testIndent.setCommodityId(UUID.randomUUID().toString());
        testIndent.setCommodityPrice(20);
        testIndent.setCarriage(10);
        testIndent.setIndentAddress("xx市xx路xx号");
        testIndent.setIndentPrice(testIndent.getCommodityPrice() + testIndent.getCarriage());
        testIndent.setIndentTime(new Date());
        testIndent.setUserId(userId);
        testIndent.setPhoneNumber("13000000000");
        testIndent.setUserName(userName);

        Response createIndentResponse = doPost(BASE_PATH + "entity", userToken,
                        JsonUtil.getJsonStringFromPojo(testIndent));
        assertEquals("Create indent failed", Status.OK.getStatusCode(), createIndentResponse.getStatus());

        indent = CommonUtil.getEntityFromResponse(createIndentResponse, Indent.class);
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Response updateIndentResponse = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(indent));
        assertEquals("Update indent failed", Status.OK.getStatusCode(), updateIndentResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testUpdateIndentPrice() {
        Response updateIndentResponse = doPut(BASE_PATH + "price", userToken, JsonUtil.getJsonStringFromPojo(indent));
        assertEquals("Update indent price failed", Status.OK.getStatusCode(), updateIndentResponse.getStatus());
    }

    @Test
    @Order(30)
    public void testUpdatePayType() {
        Response updateIndentResponse = doPut(BASE_PATH + "paytype", userToken, JsonUtil.getJsonStringFromPojo(indent));
        assertEquals("Update indent pay type failed", Status.OK.getStatusCode(), updateIndentResponse.getStatus());
    }

    @Test
    @Order(40)
    public void testUpdateIndentStatus() {
        indent.setStatus(IndentStatusEnum.PAID.value());
        Response updateIndentResponse = doPut(BASE_PATH + "status", userToken, JsonUtil.getJsonStringFromPojo(indent));
        assertEquals("Update indent status failed", Status.OK.getStatusCode(), updateIndentResponse.getStatus());
    }

    @Test
    @Order(50)
    public void testGetIndentById() {
        String path = BASE_PATH + "entity/" + indent.getId();
        Response response = doGet(path, userToken);
        assertEquals("Get indent by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(60)
    public void testGetIndentsForUser() {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        Response response = doGetWithQueryParam(BASE_PATH + "entity", userToken, queryParam);
        assertEquals("Get indents for user failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(70)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + indent.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete indent failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
