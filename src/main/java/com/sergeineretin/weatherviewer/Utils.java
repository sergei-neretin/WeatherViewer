package com.sergeineretin.weatherviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergeineretin.weatherviewer.exceptions.SessionCookieNotFoundException;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.Arrays;
import java.util.Optional;

public class Utils {
    private Utils() {}
    public static final Long SESSION_TIME_IN_HOURS = 6L;
    public static final int ZERO_CELSIUS_IN_KELVINS = 273;
    public static final String API_KEY = "9605e0b1cffa118684762af53f99d2c4";
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
    public static ITemplateEngine buildTemplateEngine(ServletContext servletContext) {
        IWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
    public static WebContext buildWebContext(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        return new WebContext(webExchange);
    }

    public static String getSessionId(HttpServletRequest req) {
        if (req.getCookies() == null) {
            throw new SessionCookieNotFoundException("required cookie not found");
        }
        Optional<Cookie> sessionId = Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals("sessionId"))
                .findFirst();
        if (sessionId.isPresent()) {
            return sessionId.get().getValue();
        } else {
            throw new SessionCookieNotFoundException("required cookie not found");
        }
    }

    public static void removeSessionIdCookie(HttpServletResponse resp) {
        Cookie sessionCookieRemove = new Cookie("sessionId", "");
        sessionCookieRemove.setMaxAge(0);
        resp.addCookie(sessionCookieRemove);
    }
}
