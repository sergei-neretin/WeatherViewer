package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionDao {
    void saveSession(Session session);
    Optional<Session> findById(String sessionId);
    List<Session> findAllSessions();
    void deleteById(String sessionId);
}
