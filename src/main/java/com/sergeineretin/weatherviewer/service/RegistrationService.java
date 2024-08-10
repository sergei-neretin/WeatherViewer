package com.sergeineretin.weatherviewer.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;


public class RegistrationService {
    private final UserDao userDao = new UserDaoImpl();
    private final ModelMapper mapper = new ModelMapper();

    public UserDto register(UserRegistrationDto userRegistrationDto) {
        String password = userRegistrationDto.getPassword();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        User user = mapper.map(userRegistrationDto, User.class);
        user.setPassword(bcryptHashString);
        userDao.save(user);
        return mapper.map(user, UserDto.class);
    }
}
