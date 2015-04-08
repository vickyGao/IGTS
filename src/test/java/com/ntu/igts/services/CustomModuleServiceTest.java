package com.ntu.igts.services;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.CustomModule;
import com.ntu.igts.test.Order;

public class CustomModuleServiceTest extends TestBase {

    @Resource
    private CustomModuleService customModuleService;
    private static CustomModule customModule;

    @Test
    @Order(0)
    public void testCreate() {
        CustomModule testCustomModule = new CustomModule();
        testCustomModule.setKeyword("数码产品");
        testCustomModule.setTitle("电子风暴");
        testCustomModule.setDisplaySequence(0);
        testCustomModule.setDisplayAmount(5);

        CustomModule insertedCustomModule = customModuleService.create(testCustomModule);
        assertNotNull("Create customModule failed", insertedCustomModule);

        customModule = insertedCustomModule;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        customModule.setTitle(customModule.getTitle() + " update");
        CustomModule updatedCustomModule = customModuleService.update(customModule);
        assertNotNull("Update customModule failed", updatedCustomModule);
    }

    @Test
    @Order(20)
    public void testGetById() {
        CustomModule returnCustomModule = customModuleService.getById(customModule.getId());
        assertNotNull("Get customModule by id failed", returnCustomModule);
    }

    @Test
    @Order(30)
    public void testGetCustomModules() {
        List<CustomModule> customModuleList = customModuleService.getCustomModules();
        assertNotNull("Get customModule list failed", customModuleList);
        assertTrue("Get customModule list failed", customModuleList.size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean customModuleDeleteFlag = customModuleService.delete(customModule.getId());
        assertTrue("Delete customModule failed", customModuleDeleteFlag);
    }
}
