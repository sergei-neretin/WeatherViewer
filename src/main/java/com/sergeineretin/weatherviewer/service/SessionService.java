package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class SessionService {
    private final SessionDao sessionDao = new SessionDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    private static SessionService instance;
    private SessionService() {}

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public SessionDto getSessionOrDelete(String sessionId) {
        Optional<Session> session = sessionDao.findById(sessionId);
        if(session.isPresent()) {
            return modelMapper.map(session.get(), SessionDto.class);
        } else {
            throw new SessionExpiredException("Session expired");
        }
    }

    public UserDto getUserOrDeleteSession(String sessionId) {
        Optional<Session> session = sessionDao.findById(sessionId);
        if(session.isPresent()) {
            User user = session.get().getUser();
            return modelMapper.map(user, UserDto.class);
        } else {
            throw new SessionExpiredException("Session expired");
        }
    }

    public void deleteSession(String sessionId) {
        sessionDao.deleteById(sessionId);
    }
}
