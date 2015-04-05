package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.Bill;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class BillResourceTest extends TestBase {

    private static final String BASE_PATH = "bill/";

    private static Bill bill;

    @Test
    @Order(0)
    public void testCreate() {
        Bill testBill = new Bill();
        testBill.setAmount(-10);
        testBill.setContent("与XXX交易");
        testBill.setUserId(userId);
        Response createBillResponse = doPost(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(testBill));
        assertEquals("Create bill failed", Status.OK.getStatusCode(), createBillResponse.getStatus());

        bill = CommonUtil.getEntityFromResponse(createBillResponse, Bill.class);
    }

    @Test
    @Order(10)
    public void testGetBillsForUser() {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        Response response = doGetWithQueryParam(BASE_PATH + "entity", userToken, queryParam);
        assertEquals("Get bills for user failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(20)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + bill.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete bill failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
