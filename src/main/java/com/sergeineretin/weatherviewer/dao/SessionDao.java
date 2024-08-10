package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.Session;

import java.util.Optional;

public interface SessionDao {
    Optional<Session> createSession(Session session);
    Optional<Session> findById(String sessionId);
}
