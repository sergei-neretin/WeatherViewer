package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;

import java.util.Optional;

public interface SessionDao {
    Optional<Session> createSession(Session session);
    Optional<Session> findById(Long sessionId);
}
