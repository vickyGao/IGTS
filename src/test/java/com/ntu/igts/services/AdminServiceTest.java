package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Admin;
import com.ntu.igts.test.Order;

public class AdminServiceTest extends TestBase {

    @Resource
    private AdminService adminService;
    private static Admin admin;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        Admin testAdmin = new Admin();
        testAdmin.setAdminName("user" + randomNumber);
        testAdmin.setAdminPassword("password");
        Admin insertedAdmin = adminService.create(testAdmin);
        assertNotNull("Create admin fail", insertedAdmin);

        admin = insertedAdmin;
    }
}
