package com.ntu.igts.services;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.ntu.igts.model.User;
import com.ntu.igts.test.OrderedRunner;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(OrderedRunner.class)
public abstract class TestBase extends JerseyTest {

    @Resource
    protected UserService userService;
    protected static User user;

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.ntu.igts");
    }

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
