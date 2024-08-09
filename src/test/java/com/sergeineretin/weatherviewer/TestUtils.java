package com.sergeineretin.weatherviewer;

import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.ZonedDateTime;

public class TestUtils {

    private TestUtils() {}

    public static final Long SESSION_TIME_IN_HOURS = 6L;
    private static final SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(Location.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static UserRegistrationDto getUser() {
        return UserRegistrationDto.builder()
                .login("John Doe")
                .password("password")
                .build();
    }

    public static SessionDto getExpiredSession() {
        UserRegistrationDto user = getUser();
        return SessionDto.builder()
                .user(user)
                .expiresAt(ZonedDateTime.now())
                .build();
    }

    public static SessionDto getSession() {
        ZonedDateTime time = ZonedDateTime.now();
        time = time.plusHours(SESSION_TIME_IN_HOURS);
        UserRegistrationDto user = getUser();
        return SessionDto.builder()
                .user(user)
                .expiresAt(time)
                .build();
    }
}
