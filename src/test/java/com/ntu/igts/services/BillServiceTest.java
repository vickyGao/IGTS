package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Bill;
import com.ntu.igts.test.Order;

public class BillServiceTest extends TestBase {

    @Resource
    private BillService billService;
    private static Bill bill;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpUser();

        Bill testBill = new Bill();
        testBill.setAmount(-10);
        testBill.setContent("与XXX交易");
        testBill.setUserId(user.getId());

        Bill insertedBill = billService.create(testBill);
        assertNotNull("Create bill failed", insertedBill);

        bill = insertedBill;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        bill.setContent(bill.getContent() + " update");
        Bill updatedBill = billService.update(bill);
        assertNotNull("Update bill failed", updatedBill);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Bill returnBill = billService.getById(bill.getId());
        assertNotNull("Get bill by id failed", returnBill);
    }

    @Test
    @Order(30)
    public void testGetByUserId() {
        mockUpUser();

        List<Bill> billList = billService.getByUserId(user.getId());
        assertNotNull("Get bill list by user id failed", billList);
        assertTrue("Get bill list by user id failed", billList.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        mockUpUser();

        boolean userDeleteFlag = userService.delete(user.getId());
        assertTrue("Delete user failed", userDeleteFlag);

        boolean billDeleteFlag = billService.delete(bill.getId());
        assertTrue("Delete bill failed", billDeleteFlag);
    }
}
