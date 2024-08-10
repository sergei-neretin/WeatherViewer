package com.sergeineretin.weatherviewer;

import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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

    public static UserRegistrationDto getUser1() {
        return UserRegistrationDto.builder()
                .login("John Doe")
                .password("password1")
                .build();
    }
    public static UserRegistrationDto getUser2() {
        return UserRegistrationDto.builder()
                .login("Jane Doe")
                .password("password2")
                .build();
    }

    public static SessionDto getExpiredSession() {
        UserRegistrationDto user = getUser1();
        return SessionDto.builder()
                .user(user)
                .expiresAt(ZonedDateTime.now())
                .build();
    }

    public static SessionDto getSession() {
        ZonedDateTime time = ZonedDateTime.now();
        time = time.plusHours(SESSION_TIME_IN_HOURS);
        UserRegistrationDto user = getUser1();
        return SessionDto.builder()
                .user(user)
                .expiresAt(time)
                .build();
    }

    public static void resetDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "")) {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP ALL OBJECTS DELETE FILES");
            stmt.execute("RUNSCRIPT FROM 'classpath:/schema.sql'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
