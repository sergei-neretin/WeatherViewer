package com.sergeineretin.weatherviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


public class Utils {
    private Utils() {}
    public static final int ZERO_CELSIUS_IN_KELVINS = 273;
    @Getter
    private static final SessionFactory sessionFactory;
    @Getter
    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
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
    public static WebContext buildWebContext(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        return new WebContext(webExchange);
    }
}
