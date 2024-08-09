package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;

public class RegistrationService {
    private UserDao userDao = new UserDaoImpl();
    private ModelMapper mapper = new ModelMapper();

    public UserDto register(UserRegistrationDto userRegistrationDto) {
        User user = mapper.map(userRegistrationDto, User.class);
        User savedUser = userDao.save(user);
        return mapper.map(savedUser, UserDto.class);
    }
}
