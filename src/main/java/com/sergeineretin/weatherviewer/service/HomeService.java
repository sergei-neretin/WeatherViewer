package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.model.Session;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class HomeService {
    private SessionDao sessionDao = new SessionDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    public SessionDto getSessionOrDelete(Long sessionId) {
        Optional<Session> session = sessionDao.findById(sessionId);
        if(session.isPresent()) {
            return modelMapper.map(session.get(), SessionDto.class);
        } else {
            return new SessionDto();
        }
    }
}
