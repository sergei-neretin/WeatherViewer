package com.sergeineretin.weatherviewer;

import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
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
    public static final String OPEN_WEATHER_API_LOCATIONS_JSON = "[{\"name\":\"Novosibirsk\",\"local_names\":{\"ar\":\"نوفوسيبيرسك\",\"lt\":\"Novosibirskas\",\"be\":\"Новасібірск\",\"hi\":\"नोवोसिबिर्स्क\",\"ru\":\"Новосибирск\",\"fi\":\"Novosibirsk\",\"kk\":\"Новосібір\",\"en\":\"Novosibirsk\",\"pt\":\"Novosibirsk\",\"he\":\"נובוסיבירסק\",\"kn\":\"ನೋವೋಸಿಬಿರ್ಸ್ಕ್\",\"ja\":\"ノヴォシビルスク管区\",\"sl\":\"Novosibirsk\",\"hr\":\"Novosibirsk\",\"et\":\"Novosibirsk\",\"de\":\"Nowosibirsk\",\"lv\":\"Novosibirska\",\"es\":\"Novosibirsk\",\"oc\":\"Novosibirsk\",\"uk\":\"Новосибірськ\",\"cs\":\"Novosibirsk\",\"ku\":\"Novosîbîrsk\",\"fr\":\"Novossibirsk\",\"pl\":\"Nowosybirsk\",\"ko\":\"노보시비르스크\",\"uz\":\"Novosibirsk\",\"az\":\"Novosibirsk\",\"ca\":\"Districte urbà de Novossibirsk\",\"sk\":\"Novosibirsk\",\"da\":\"Novosibirsk\",\"ro\":\"Novosibirsk\"},\"lat\":55.0282171,\"lon\":82.9234509,\"country\":\"RU\",\"state\":\"Novosibirsk Oblast\"},{\"name\":\"Novosibirsk\",\"local_names\":{\"ca\":\"Novossibirsk\",\"en\":\"Novosibirsk\",\"ja\":\"ノヴォシビルスク\",\"ku\":\"Novosîbîrsk\",\"ru\":\"Новосибирск\",\"zh\":\"新西伯利亚\"},\"lat\":54.96781445,\"lon\":82.95159894278376,\"country\":\"RU\",\"state\":\"Novosibirsk Oblast\"}]";
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
    public static LocationWithTemperature getLocation1() {
        return LocationWithTemperature.builder()
                .latitude(new BigDecimal("55.0328"))
                .longitude(new BigDecimal("82.9282"))
                .name("Novosibirsk")
                .build();
    }
    public static LocationWithTemperature getLocation2() {
        return LocationWithTemperature.builder()
                .latitude(new BigDecimal("55.7522"))
                .longitude(new BigDecimal("37.6156"))
                .name("Moscow")
                .build();
    }


}
