package com.sergeineretin.weatherviewer;

import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TestUtils {

    private TestUtils() {}

    public static final Long SESSION_TIME_IN_HOURS = 6L;

    public static final String OPEN_WEATHER_API_JSON1 = "{\"coord\":{\"lon\":82.9282,\"lat\":55.0328},\"weather\":[{\"id\":520,\"main\":\"Rain\",\"description\":\"light intensity shower rain\",\"icon\":\"09d\"}],\"base\":\"stations\",\"main\":{\"temp\":290.84,\"feels_like\":290.44,\"temp_min\":290.84,\"temp_max\":290.84,\"pressure\":1008,\"humidity\":68,\"sea_level\":1008,\"grnd_level\":991},\"visibility\":10000,\"wind\":{\"speed\":2,\"deg\":240},\"clouds\":{\"all\":75},\"dt\":1723361865,\"sys\":{\"type\":1,\"id\":8958,\"country\":\"RU\",\"sunrise\":1723330530,\"sunset\":1723385496},\"timezone\":25200,\"id\":1496747,\"name\":\"Novosibirsk\",\"cod\":200}";
    public static final String OPEN_WEATHER_API_JSON2 = "{\"coord\":{\"lon\":37.6156,\"lat\":55.7522},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":287.78,\"feels_like\":287.44,\"temp_min\":287.78,\"temp_max\":289.08,\"pressure\":1003,\"humidity\":82,\"sea_level\":1003,\"grnd_level\":985},\"visibility\":10000,\"wind\":{\"speed\":5.55,\"deg\":298,\"gust\":8.81},\"clouds\":{\"all\":99},\"dt\":1723445940,\"sys\":{\"type\":2,\"id\":2094500,\"country\":\"RU\",\"sunrise\":1723427768,\"sunset\":1723482787},\"timezone\":10800,\"id\":524901,\"name\":\"Moscow\",\"cod\":200}";
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

    public static User getUser1() {
        return User.builder()
                .login("John Doe")
                .password("password1")
                .build();
    }

    public static UserRegistrationDto getUser1Dto() {
        return UserRegistrationDto.builder()
                .login("John Doe")
                .password("password1")
                .build();
    }
    public static UserRegistrationDto getUser2Dto() {
        return UserRegistrationDto.builder()
                .login("Jane Doe")
                .password("password2")
                .build();
    }

    public static Session getExpiredSession() {
        return Session.builder()
                .expiresAt(ZonedDateTime.now())
                .build();
    }

    public static SessionDto getSessionDto() {
        ZonedDateTime time = ZonedDateTime.now();
        time = time.plusHours(SESSION_TIME_IN_HOURS);
        UserRegistrationDto user = getUser1Dto();
        return SessionDto.builder()
                .user(user)
                .expiresAt(time)
                .build();
    }

    public static Session getSession() {
        ZonedDateTime time = ZonedDateTime.now();
        time = time.plusHours(SESSION_TIME_IN_HOURS);
        return Session.builder()
                .expiresAt(time)
                .build();
    }
    public static LocationDto getLocation1() {
        return LocationDto.builder()
                .latitude(new BigDecimal("55.0328"))
                .longitude(new BigDecimal("82.9282"))
                .name("Novosibirsk")
                .build();
    }
    public static LocationDto getLocation2() {
        return LocationDto.builder()
                .latitude(new BigDecimal("55.7522"))
                .longitude(new BigDecimal("37.6156"))
                .name("Moscow")
                .build();
    }


}
