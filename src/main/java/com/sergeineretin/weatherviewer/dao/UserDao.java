package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.User;

public interface UserDao {
    public User getUserById(int id);
    public void insertUser(User user);
}
