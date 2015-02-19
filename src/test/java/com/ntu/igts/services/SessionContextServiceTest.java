package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Admin;
import com.ntu.igts.model.SessionContext;
import com.ntu.igts.test.Order;

public class SessionContextServiceTest extends TestBase {

    @Resource
    private SessionContextService sessionContextService;
    @Resource
    private AdminService adminService;
    private static SessionContext sessionContext;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString();

        Admin admin = new Admin();
        admin.setAdminName("test-admin" + randomNumber);
        admin.setAdminPassword("123456");
        Admin insertedAdmin = adminService.create(admin);
        assertNotNull("Create admin failed", insertedAdmin);

        SessionContext insertedSessionContext = sessionContextService.create(insertedAdmin.getId());
        assertNotNull("Create sessionContext failed", insertedSessionContext);

        sessionContext = insertedSessionContext;
    }

    @Test
    @Order(1)
    public void testFlushSessionContext() {
        long originalExpireTime = sessionContext.getExpireTime().getTime();
        SessionContext flushedSessionContext = sessionContextService.flushSessionContext(sessionContext);
        assertNotNull("Flush sessionContext failed", flushedSessionContext);
        assertTrue("Flush sessionContext failed", flushedSessionContext.getExpireTime().getTime() > originalExpireTime);
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Date date = new Date();
        sessionContext.setLoginTime(date);
        SessionContext updatedSessionContext = sessionContextService.update(sessionContext);
        assertNotNull("Update sessionContext failed", updatedSessionContext);
        assertEquals("Update sessionContext failed", sessionContext.getLoginTime(),
                        updatedSessionContext.getLoginTime());
    }

    @Test
    @Order(3)
    public void testGetByToken() {
        SessionContext returnSessionContext = sessionContextService.getByToken(sessionContext.getToken());
        assertNotNull("Get sessionContext by token failed", returnSessionContext);
    }

    @Test
    @Order(4)
    public void testDelete() {
        boolean adminDeleteFlag = adminService.delete(sessionContext.getUserId());
        assertTrue("Delete admin failed", adminDeleteFlag);
        boolean sessionContextDeleteFlag = sessionContextService.delete(sessionContext.getToken());
        assertTrue("Delete sessionContext failed", sessionContextDeleteFlag);
    }
}
