package com.ntu.igts.dbinit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ntu.igts.model.Admin;
import com.ntu.igts.model.Role;
import com.ntu.igts.model.User;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.RoleService;
import com.ntu.igts.services.UserService;

@Component
public class InitData {

    private static final Logger LOGGER = Logger.getLogger(InitData.class);

    private static Role roleUser;
    private static Role roleAdmin;

    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;

    public void createStandardData() {
        LOGGER.info("Start to init standard data");
        createRole();
        createUser();
        createAdmin();
        LOGGER.info("Init standard data finished");
    }

    public void createSampleData() {
        LOGGER.info("Start to init sample data");
        LOGGER.info("Sample standard data finished");
    }

    private void createUser() {
        User user = new User();
        user.setUserName("user");
        user.setPassword("password");
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleUser);
        user.setRoles(roles);
        User insertedUser = userService.create(user);
        LOGGER.info("Created user " + insertedUser.getUserName());
    }

    private void createAdmin() {
        Admin admin = new Admin();
        admin.setAdminName("user");
        admin.setAdminPassword("password");
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleAdmin);
        admin.setRoles(roles);
        Admin insertedAdmin = adminService.create(admin);
        LOGGER.info("Created admin " + insertedAdmin.getAdminName());
    }

    private void createRole() {
        Role role1 = new Role();
        role1.setRoleName("user");
        role1.setRoleStandardName("USER");
        roleUser = roleService.create(role1);
        LOGGER.info("Created role " + roleUser.getRoleName());

        Role role2 = new Role();
        role2.setRoleName("admin");
        role2.setRoleStandardName("ADMIN");
        roleAdmin = roleService.create(role2);
        LOGGER.info("Created role " + roleAdmin.getRoleName());
    }
}
