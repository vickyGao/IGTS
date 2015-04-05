package com.ntu.igts.resource;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.ntu.igts.model.Address;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.JsonUtil;

public class AddressResourceTest extends TestBase {

    private static final String BASE_PATH = "address/";

    private static Address address;

    @Test
    @Order(0)
    public void testCreate() {
        Address testAddress = new Address();
        testAddress.setAddressCountry("中国");
        testAddress.setAddressProvince("上海");
        testAddress.setAddressCity("上海");
        testAddress.setAddressDetail("XXX区XX镇XX路XX号101");
        testAddress.setPostcode("210102");
        testAddress.setPhoneNumber("13000000000");
        testAddress.setUserId(userId);

        Response createAddressResponse = doPost(BASE_PATH + "entity", userToken,
                        JsonUtil.getJsonStringFromPojo(testAddress));
        assertEquals("Create address failed", Status.OK.getStatusCode(), createAddressResponse.getStatus());

        address = CommonUtil.getEntityFromResponse(createAddressResponse, Address.class);
    }

    @Test
    @Order(10)
    public void testUdpate() {
        address.setAddressDetail(address.getAddressDetail() + " update");
        Response updateAddressResponse = doPut(BASE_PATH + "entity", userToken, JsonUtil.getJsonStringFromPojo(address));
        assertEquals("Update address failed", Status.OK.getStatusCode(), updateAddressResponse.getStatus());
    }

    @Test
    @Order(20)
    public void testGetAddressById() {
        String path = BASE_PATH + "entity/" + address.getId();
        Response response = doGet(path, userToken);
        assertEquals("Get address by id failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(30)
    public void testGetAddressesForUser() {
        Response response = doGet(BASE_PATH + "entity", userToken);
        assertEquals("Get addresses for user failed", Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(40)
    public void testDelete() {
        String path = BASE_PATH + "entity/" + address.getId();
        Response response = doDelete(path, userToken);
        assertEquals("Delete addresses failed", Status.OK.getStatusCode(), response.getStatus());
    }
}
