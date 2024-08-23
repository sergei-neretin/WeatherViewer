package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.model.Session;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.Optional;

public class LoginService {
    SessionDao sessionDao = new SessionDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    public SessionDto login(SessionDto sessionDto) {
        Session session = modelMapper.map(sessionDto, Session.class);
        Optional<Session> optionalSession = sessionDao.createSession(session);
        if (optionalSession.isPresent()) {
            return modelMapper.map(optionalSession.get(), SessionDto.class);
        } else {
            throw new UserNotFoundException("Invalid username or password");
        }
    }

    public SessionDto buildSessionDto(String login, String password) {
        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .login(login)
                .password(password)
                .build();
        ZonedDateTime expiredTime = ZonedDateTime.now();
        expiredTime = expiredTime.plusHours(Utils.SESSION_TIME_IN_HOURS);
        return SessionDto.builder()
                .user(userDto)
                .expiresAt(expiredTime)
                .build();
    }
}
