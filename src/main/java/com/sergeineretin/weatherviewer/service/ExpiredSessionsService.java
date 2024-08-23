package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.model.Session;

import java.time.ZonedDateTime;
import java.util.List;

public class ExpiredSessionsService {
    private ExpiredSessionsService() {}
    public static void deleteExpiredSessions(){
        SessionDao sessionDao = new SessionDaoImpl();
        List<Session> allSessions = sessionDao.findAllSessions();
        for(Session session : allSessions) {
            if (session.getExpiresAt().isAfter(ZonedDateTime.now())) {
                sessionDao.deleteById(session.getId());
            }
        }
    }
}
