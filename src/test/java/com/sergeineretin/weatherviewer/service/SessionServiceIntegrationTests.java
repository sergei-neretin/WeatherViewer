package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceIntegrationTests {
    SessionService underTest = SessionService.getInstance();

    @BeforeEach
    public void before() {
        try (org.hibernate.Session session = TestUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DELETE FROM sessions", Session.class).executeUpdate();
            session.createNativeQuery("DELETE FROM users", User.class).executeUpdate();
            session.createNativeQuery("DELETE FROM locations", User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testThatSessionServiceGetUserOrDeleteSessionReturnsUser() {
        String sessionId;
        try (org.hibernate.Session session = TestUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user1 = TestUtils.getUser1();
            session.persist(user1);
            Session session1 = TestUtils.getSession();
            session1.setUser(user1);
            session.persist(session1);
            sessionId = session1.getId();
            session.getTransaction().commit();
        }

        UserDto result = underTest.getUserOrDeleteSession(sessionId);

        assertEquals(TestUtils.getUser1Dto().getLogin(), result.getLogin());
    }

    @Test
    public void testThatHomeServiceGetSessionOrDeleteThrowsExceptionWhenSessionIsExpired() {
        String sessionId;
        try (org.hibernate.Session session = TestUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user1 = TestUtils.getUser1();
            session.persist(user1);
            Session session1 = TestUtils.getExpiredSession();
            session1.setUser(user1);
            session.persist(session1);
            sessionId = session1.getId();
            session.getTransaction().commit();
        }
        assertThrows(SessionExpiredException.class, () -> underTest.getUserOrDeleteSession(sessionId));
    }
}
