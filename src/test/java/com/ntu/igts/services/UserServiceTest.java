package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.User;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.test.Order;
import com.ntu.igts.utils.MD5Util;

public class UserServiceTest extends TestBase {

    @Resource
    private UserService userService;
    private static User user;

    @Test
    @Order(0)
    public void testCreate() {
        String randomNumber = UUID.randomUUID().toString().replace("-", "");

        User testUser = new User();
        testUser.setUserName("user" + randomNumber);
        testUser.setPassword("password");
        testUser.setAge(20);
        User insertedUser = userService.create(testUser);
        assertNotNull("Create user failed", insertedUser);

        user = insertedUser;
    }

    @Test
    @Order(1)
    public void testUpdatePassword() {
        User updatedUser = userService.updatePassword(user.getId(), "password2");
        assertNotNull("Update user failed", updatedUser);
        assertEquals("Update user failed", MD5Util.getMd5("password2"), updatedUser.getPassword());
    }

    @Test
    @Order(10)
    public void testUpdate() {
        user.setAge(21);
        User updatedUser = userService.update(user);
        assertNotNull("Update user failed", updatedUser);
        assertEquals("Update user failed", 21, updatedUser.getAge());
    }

    @Test
    @Order(20)
    public void testGetUserById() {
        User returnUser = userService.getUserById(user.getId());
        assertNotNull("Get user by id failed", returnUser);
        assertEquals("Get user by id failed", user.getId(), returnUser.getId());
    }

    @Test
    @Order(30)
    public void testGetUsers() {
        List<User> users = userService.getUsers();
        assertNotNull("Get user failed", users);
        assertTrue("Get users failed", users.size() > 0);
    }

    @Test
    @Order(40)
    public void testGetByPage() {
        Query query = new Query();
        query.setSearchTerm(user.getUserName());
        query.setPage(0);
        query.setSize(5);
        query.setSortBy(SortByEnum.USER_NAME);
        query.setOrderBy(OrderByEnum.ASC);
        Page<User> userPage = userService.getByPage(query);
        assertNotNull("Get users by page failed", userPage);
        assertTrue("Get users failed", userPage.getContent().size() > 0);
    }

    @Test
    @Order(50)
    public void testGetUserByUserName() {
        User returnUser = userService.getUserByUserName(user.getUserName());
        assertNotNull("Get user by user name failed", returnUser);
        assertEquals("Get user by user name failed", user.getUserName(), returnUser.getUserName());
    }

    @Test
    @Order(60)
    public void testDelete() {
        boolean flag = userService.delete(user.getId());
        assertTrue("Delete user failed", flag);
    }
}
