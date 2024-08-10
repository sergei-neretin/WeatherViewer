package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.model.Session;
import org.modelmapper.ModelMapper;

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
}
