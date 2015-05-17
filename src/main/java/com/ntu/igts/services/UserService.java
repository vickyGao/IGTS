package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.User;
import com.ntu.igts.model.container.Query;

public interface UserService {

    public User create(User user);

    public User update(User user);

    public User updatePassword(String userId, String password);

    public boolean delete(String userId);

    public User getUserById(String userId);

    public User getUserDetailById(String userId);

    public List<User> getUsers();

    public Page<User> getByPage(Query query);

    public User getUserByUserName(String userName);

    public int getTotalCount();

    public User lockUserMoney(double money, String userId);

    public User unLockUserMoney(double money, String userId);
}
