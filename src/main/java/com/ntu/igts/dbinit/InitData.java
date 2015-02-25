package com.ntu.igts.dbinit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ntu.igts.model.Admin;
import com.ntu.igts.model.User;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.UserService;

@Component
public class InitData {

    private static final Logger LOGGER = Logger.getLogger(InitData.class);

    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;

    public void createStandardData() {
        try {
            LOGGER.info("Start to init standard data");
            createUser();
            Thread.sleep(1000);
            createAdmin();
            LOGGER.info("Init standard data finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createSampleData() {
        LOGGER.info("Start to init sample data");
        LOGGER.info("Sample standard data finished");
    }

    private void createUser() {
        User user = new User();
        user.setUserName("user");
        user.setPassword("password");
        User insertedUser = userService.create(user);
        LOGGER.info("Created user " + insertedUser.getUserName());
    }

    private void createAdmin() {
        Admin admin = new Admin();
        admin.setAdminName("user");
        admin.setAdminPassword("password");
        Admin insertedAdmin = adminService.create(admin);
        LOGGER.info("Created admin " + insertedAdmin.getAdminName());
    }
}
