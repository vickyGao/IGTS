package com.ntu.igts.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
    public User create(User user) {
        String passwordMD5 = MD5Util.getMd5(user.getPassword());
        user.setSellerLevel(passwordLevel(passwordMD5));
        user.setPassword(passwordMD5);
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

    private int passwordLevel(String password){
        if(password.matches("((?=[\\x21-\\x7e]+)[^A-Za-z0-9])")){
            return 3;
        }else if(password.matches("^[a-zA-Z0-9]{6,16}+$")){
            return 2;
        }else if(password.matches("^[A-Za-z]+$")){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public User updatePassword(String userId, String password) {
        String passwordMD5 = MD5Util.getMd5(password);
        int count = userRepository.updatePassword(userId, passwordMD5);
        if (count > 0) {
            User updateUser = getUserById(userId);
            updateUser.setSellerLevel(passwordLevel(passwordMD5));
            update(updateUser);
            return getUserById(userId);
        } else {
            return null;
        }
    }

    @Override
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

    @Override
    public int getTotalCount() {
        return userRepository.getTotalCouont();
    }

    @Override
    public User lockUserMoney(double money, String userId) {
        User user = getUserById(userId);
        user.setMoney(user.getMoney() - money);
        user.setLockedMoney(user.getLockedMoney() + money);
        return update(user);
    }

    @Override
    public User unLockUserMoney(double money, String userId) {
        User user = getUserById(userId);
        user.setMoney(user.getMoney() + money);
        user.setLockedMoney(user.getLockedMoney() - money);
        return update(user);
    }

}
