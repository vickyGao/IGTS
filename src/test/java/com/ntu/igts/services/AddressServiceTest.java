package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Address;
import com.ntu.igts.test.Order;

public class AddressServiceTest extends TestBase {

    @Resource
    private AddressService addressService;
    private static Address address;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpUser();

        Address testAddress = new Address();
        testAddress.setAddressCountry("中国");
        testAddress.setAddressProvince("上海");
        testAddress.setAddressCity("上海");
        testAddress.setAddressDetail("XXX区XX镇XX路XX号101");
        testAddress.setPostcode("210102");
        testAddress.setPhoneNumber("13000000000");
        testAddress.setUserId(user.getId());

        Address insertedAddress = addressService.create(testAddress);
        assertNotNull("Create address failed", insertedAddress);

        address = insertedAddress;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        address.setAddressDetail(address.getAddressDetail() + "update");
        Address updatedAddress = addressService.update(address);
        assertNotNull("Update address failed", updatedAddress);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Address returnAddress = addressService.getById(address.getId());
        assertNotNull("Get address by id failed", returnAddress);
    }

    @Test
    @Order(30)
    public void testGetByUserId() {
        mockUpUser();

        List<Address> addresses = addressService.getByUserId(user.getId());
        assertNotNull("Get addressed by user id failed", addresses);
        assertTrue("Get addressed by user id failed", addresses.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean userDeleteFlag = userService.delete(user.getId());
        assertTrue("Delete user failed", userDeleteFlag);

        boolean addressDeleteFlag = addressService.delete(address.getId());
        assertTrue("Delete address failed", addressDeleteFlag);
    }

}
