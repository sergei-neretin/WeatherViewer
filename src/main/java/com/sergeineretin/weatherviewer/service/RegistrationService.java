package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;

public class RegistrationService {
    UserDao userDao = new UserDaoImpl();
    ModelMapper mapper = new ModelMapper();

    public void register(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        userDao.save(user);
    }
}
