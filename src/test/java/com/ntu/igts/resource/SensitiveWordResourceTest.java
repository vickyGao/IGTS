package com.ntu.igts.resource;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.model.SensitiveWord;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class SensitiveWordResourceTest extends TestBase {

    private static final String BASE_PATH = "sensitiveword/";

    private static SensitiveWord sensitiveWord;

    @Test
    @Order(0)
    public void testCreate() {
        SensitiveWord testSensitiveWord = new SensitiveWord();
        testSensitiveWord.setWord("testSensitiveWord");

        Response createSensitiveWordResponse = doPost(BASE_PATH + "entity", adminToken,
                        JsonUtil.getJsonStringFromPojo(testSensitiveWord));
        assertEquals("Create sensitive word failed", Status.OK.getStatusCode(), createSensitiveWordResponse.getStatus());

        sensitiveWord = CommonUtil.getEntityFromResponse(createSensitiveWordResponse, SensitiveWord.class);
    }

    @Test
    @Order(10)
    public void testUpdateSensitiveWordState() {
        String path = BASE_PATH + "status/" + ActiveStateEnum.ACTIVE.name() + "/" + sensitiveWord.getId();
        Response updateSensitiveWordResponse = doPut(path, adminToken);
        assertEquals("Update sensitive word failed", Status.OK.getStatusCode(), updateSensitiveWordResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetBySearchTerm() {
        Map<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("search_term", sensitiveWord.getWord());
        queryParam.put("page", "0");
        queryParam.put("size", "10");
        Response response = doGetWithQueryParam(BASE_PATH + "search_term", adminToken, queryParam);
        assertEquals("Get sensitive word by search term failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + sensitiveWord.getId();
        Response response = doDelete(path, adminToken);
        assertEquals("Delete sensitive word failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
