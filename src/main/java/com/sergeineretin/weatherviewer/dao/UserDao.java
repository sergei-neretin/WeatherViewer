package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.User;

public interface UserDao {
    User getUserById(long id);
    User save(User user);
    void deleteUserById(long id);

    User getUserByLogin(String login);
}
