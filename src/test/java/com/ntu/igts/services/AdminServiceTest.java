package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.MD5Util;

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

    @Test
    @Order(10)
    public void testUpdate() {
        admin.setAdminPassword("password2");
        Admin updatedAdmin = adminService.update(admin);
        assertNotNull("Update admin fail", updatedAdmin);
        assertEquals("Update admin fail", MD5Util.getMd5("password2"), updatedAdmin.getAdminPassword());
    }

    @Test
    @Order(20)
    public void testGetAdminByAdminName() {
        Admin returnAdmin = adminService.getAdminByAdminName(admin.getAdminName());
        assertNotNull("Get admin by admin name fail", returnAdmin);
        assertEquals("Get admin by admin name fail", admin.getId(), returnAdmin.getId());
    }

    @Test
    @Order(21)
    public void testGetPaginatedAdmins() {
        Query query = new Query();
        query.setSearchTerm(admin.getAdminName());
        query.setPage(0);
        query.setSize(5);
        query.setSortBy(SortByEnum.ADMIN_NAME);
        query.setOrderBy(OrderByEnum.ASC);
        Page<Admin> page = adminService.getPaginatedAdmins(query);
        assertNotNull("Get users by page failed", page);
        assertTrue("Get users failed", page.getContent().size() > 0);
    }

    @Test
    @Order(30)
    public void testDelete() {
        boolean flage = adminService.delete(admin.getId());
        assertTrue("Delete admin fail", flage);
    }
}
