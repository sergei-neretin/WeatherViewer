package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class LoginService {
    SessionDao sessionDao = new SessionDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    public SessionDto login(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        Optional<Session> session = sessionDao.createSession(user);
        if (session.isPresent()) {
            return modelMapper.map(session.get(), SessionDto.class);
        } else {
            throw new UserNotFoundException("Invalid username or password");
        }
    }
}
