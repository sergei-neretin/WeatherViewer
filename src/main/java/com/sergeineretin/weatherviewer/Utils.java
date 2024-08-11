package com.sergeineretin.weatherviewer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sergeineretin.weatherviewer.deserializer.LocationDtoCustomDeserializer;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import jakarta.servlet.ServletContext;
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

public class Utils {
    private Utils() {}
    public static final Long SESSION_TIME_IN_HOURS = 6L;
    public static final String API_KEY = "9605e0b1cffa118684762af53f99d2c4";
    @Getter
    private static final SessionFactory sessionFactory;
    @Getter
    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(
                "LocationDtoCustomDeserializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(LocationDto.class, new LocationDtoCustomDeserializer());
        mapper.registerModule(module);

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
}
