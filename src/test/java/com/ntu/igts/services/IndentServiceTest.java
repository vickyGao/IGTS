package com.ntu.igts.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.model.Indent;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.CommonUtil;

public class IndentServiceTest extends TestBase {

    @Resource
    private IndentService indentService;
    private static Indent indent;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpUser();

        Indent testIndent = new Indent();
        testIndent.setIndentNumber(CommonUtil.getIndentNumber());
        testIndent.setCommodityId(UUID.randomUUID().toString());
        testIndent.setCommodityPrice(20);
        testIndent.setCarriage(10);
        testIndent.setIndentAddress("xx市xx路xx号");
        testIndent.setIndentPrice(testIndent.getCommodityPrice() + testIndent.getCarriage());
        testIndent.setIndentTime(new Date());
        testIndent.setUserId(user.getId());
        testIndent.setPhoneNumber("13000000000");
        testIndent.setUserName(user.getUserName());
        Indent insertedIndent = indentService.create(testIndent);
        assertNotNull("Create indent failed", insertedIndent);

        indent = insertedIndent;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        indent.setDeliverTime(new Date());
        Indent updatedIndent = indentService.update(indent);
        assertNotNull("Update indent failed", updatedIndent);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Indent returnIndent = indentService.getById(indent.getId());
        assertNotNull("Get indent by id failed", returnIndent);
    }

    @Test
    @Order(30)
    public void testGetByUserId() {
        List<Indent> indentList = indentService.getByUserId(user.getId());
        assertNotNull("Get indent by user id failed", indentList);
        assertTrue("Get indent by user id failed", indentList.size() > 0);
    }

    @Test
    @Order(31)
    public void testGetPaginatedIndentByUserId() {
        Page<Indent> page = indentService.getPaginatedIndentByUserId(0, 5, user.getId());
        assertNotNull("Get paginated indent by user id failed", page);
        assertTrue("Get paginated indent by user id failed", page.getContent().size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean userDeleteFlag = userService.delete(user.getId());
        assertTrue("Delete user failed", userDeleteFlag);

        boolean indentDeleteFlag = indentService.delete(indent.getId());
        assertTrue("Delete indent failed", indentDeleteFlag);
    }
}
