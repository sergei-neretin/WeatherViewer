package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.model.Session;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SessionService {
    private final SessionDao sessionDao = new SessionDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    private static SessionService instance;
    private SessionService() {}

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public UserDto getUserOrDeleteSession(String sessionId) {
        Optional<Session> session = sessionDao.findById(sessionId);
        if (session.isPresent() && session.get().getExpiresAt().isAfter(ZonedDateTime.now())) {
            return modelMapper.map(session.get().getUser(), UserDto.class);
        } else if (session.isPresent()) {
            sessionDao.deleteById(sessionId);
            throw new SessionExpiredException("Session expired");
        } else {
            throw new SessionExpiredException("Session not found");
        }
    }

    public void deleteSession(String sessionId) {
        sessionDao.deleteById(sessionId);
    }
}
