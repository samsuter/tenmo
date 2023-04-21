package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public User getUserByName(String username) {
        return userDao.findByUsername(username);
    }

    public int getUserIdByName(String username) {
        return userDao.findIdByUsername(username);
    }

    public String getUserNameByAccountId(int accountId) {
        return userDao.getUserNameByAccountId(accountId);
    }
}
