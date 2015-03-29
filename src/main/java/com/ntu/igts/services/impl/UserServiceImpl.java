package com.ntu.igts.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.RoleEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Role;
import com.ntu.igts.model.User;
import com.ntu.igts.model.UserRole;
import com.ntu.igts.model.container.Query;
import com.ntu.igts.repository.UserRepository;
import com.ntu.igts.repository.UserRoleRepository;
import com.ntu.igts.services.RoleService;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.CommonUtil;
import com.ntu.igts.utils.MD5Util;
import com.ntu.igts.utils.StringUtil;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private UserRoleRepository userRoleRepository;
    @Resource
    private RoleService roleService;

    @Override
    @Transactional
    public User create(User user) {
        user.setPassword(MD5Util.getMd5(user.getPassword()));
        User insertedUser = userRepository.create(user);
        if (insertedUser != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(insertedUser.getId());
            List<Role> roles = roleService.getRoles();
            String roleId = CommonUtil.getRequiredRoleIdFromRoles(roles, RoleEnum.USER);
            userRole.setRoleId(roleId);
            userRoleRepository.create(userRole);
            return getUserDetailById(insertedUser.getId());
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    @Transactional
    public User updatePassword(String userId, String password) {
        int count = userRepository.updatePassword(userId, MD5Util.getMd5(password));
        if (count > 0) {
            return getUserById(userId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(String userId) {
        userRepository.delete(userId);
        User user = userRepository.findById(userId);
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User getUserDetailById(String userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            List<UserRole> userRoles = userRoleRepository.getUserRolesByUserId(userId);
            List<Role> roles = new ArrayList<Role>();
            for (UserRole userRole : userRoles) {
                roles.add(roleService.getById(userRole.getRoleId()));
            }
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getByPage(Query query) {
        if (query.getSearchTerm() == null) {
            query.setSearchTerm(StringUtil.EMPTY);
        }
        if (query.getSize() == 0) {
            query.setSize(10);
        }
        if (query.getSortBy() == null) {
            query.setSortBy(SortByEnum.USER_NAME);
        }
        if (query.getOrderBy() == null) {
            query.setOrderBy(OrderByEnum.ASC);
        }

        return userRepository.findByPage(query);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.getUserByUserName(userName);
    }

}
