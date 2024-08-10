package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.exceptions.UniqueConstraintViolationException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationServiceIntegrationTests {
    private RegistrationService underTest;
    @BeforeEach
    public void before() {
        try (org.hibernate.Session session = TestUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DELETE FROM sessions", Session.class).executeUpdate();
            session.createNativeQuery("DELETE FROM users", User.class).executeUpdate();
            session.getTransaction().commit();
        }
        underTest = new RegistrationService();
    }

    @Test
    public void testThatRegistrationServiceRegisterUserWithNotUniqueLoginThrowsException() {
        UserRegistrationDto userDto = TestUtils.getUser();
        underTest.register(userDto);
        assertThrows(UniqueConstraintViolationException.class, () -> underTest.register(userDto));
    }

    @Test
    public void testThatRegistrationServiceRegisterUserWithUniqueLoginReturnsCorrectId() {
        UserRegistrationDto userDto = TestUtils.getUser();
        UserDto result = underTest.register(userDto);
        assertEquals(3L, result.getId());
    }
}
