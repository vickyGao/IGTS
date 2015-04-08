package com.ntu.igts.services;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Role;
import com.ntu.igts.test.Order;

public class RoleServiceTest extends TestBase {

    @Resource
    private RoleService roleService;
    private static Role role;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        Role testRole = new Role();
        testRole.setRoleName("testRole");
        testRole.setRoleStandardName("TESTROLE" + randomNumber);
        Role insertedRole = roleService.create(testRole);
        assertNotNull("Create role fail", insertedRole);
        role = insertedRole;
    }

    @Test
    @Order(10)
    public void testGetById() {
        Role returnRole = roleService.getById(role.getId());
        assertNotNull("Get role by id fail", returnRole);
    }

    @Test
    @Order(20)
    public void testGetRoles() {
        List<Role> roles = roleService.getRoles();
        assertNotNull("Get roles fail", roles);
        assertTrue("Get roles fail", roles.size() > 0);
    }

    @Test
    @Order(30)
    public void testDelete() {
        boolean flag = roleService.delete(role.getId());
        assertTrue("Delete role fail", flag);
    }
}
