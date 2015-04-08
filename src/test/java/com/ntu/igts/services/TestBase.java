package com.ntu.igts.services;

import java.util.UUID;

import javax.annotation.Resource;
import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.ntu.igts.model.User;
import com.ntu.igts.test.OrderedRunner;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(OrderedRunner.class)
public abstract class TestBase extends TestCase {

    @Resource
    protected UserService userService;
    protected static User user;

    protected void mockUpUser() {
        if (user == null) {
            String randomNumber = UUID.randomUUID().toString().replace("-", "");

            User testUser = new User();
            testUser.setUserName("testUser" + randomNumber);
            testUser.setPassword("password");
            User insertedUser = userService.create(testUser);
            assertNotNull("Create user failed", insertedUser);

            user = insertedUser;
        }
    }
}
