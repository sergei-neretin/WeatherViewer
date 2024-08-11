package com.sergeineretin.weatherviewer;

import com.sergeineretin.weatherviewer.dto.SessionDto;
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

    public static final String OPEN_WEATHER_API_JSON = "{\"coord\":{\"lon\":82.9282,\"lat\":55.0328},\"weather\":[{\"id\":520,\"main\":\"Rain\",\"description\":\"light intensity shower rain\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":290.84,\"feels_like\":290.44,\"temp_min\":290.84,\"temp_max\":290.84,\"pressure\":1008,\"humidity\":68,\"sea_level\":1008,\"grnd_level\":991},\"visibility\":10000,\"wind\":{\"speed\":2,\"deg\":240},\"clouds\":{\"all\":75},\"dt\":1723361865,\"sys\":{\"type\":1,\"id\":8958,\"country\":\"RU\",\"sunrise\":1723330530,\"sunset\":1723385496},\"timezone\":25200,\"id\":1496747,\"name\":\"Novosibirsk\",\"cod\":200}";
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

}
