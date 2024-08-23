package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.exceptions.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class BaseController extends HttpServlet {
    protected ITemplateEngine templateEngine;
    protected WebContext webContext;
    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.templateEngine = buildTemplateEngine(servletContext);
    }

    protected static ITemplateEngine buildTemplateEngine(ServletContext servletContext) {
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            super.service(req, resp);
        } catch (SessionExpiredException e) {
            removeSessionIdCookie(resp);
            webContext.setVariable("authorized", false);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            webContext.setVariable("authorized", false);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (UserNotFoundException e) {
            webContext.setVariable("userError", true);
            templateEngine.process("login", webContext, resp.getWriter());
        } catch (DatabaseException e) {
            webContext.setVariable("databaseError", true);
            templateEngine.process("login", webContext, resp.getWriter());
        } catch (UniqueConstraintViolationException e) {
            log.info(e.getMessage());
            webContext.setVariable("loginAlreadyExist", true);
            templateEngine.process("register", webContext, resp.getWriter());
        }
    }

    protected String getSessionId(HttpServletRequest req) {
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

    protected void removeSessionIdCookie(HttpServletResponse resp) {
        Cookie sessionCookieRemove = new Cookie("sessionId", "");
        sessionCookieRemove.setMaxAge(0);
        resp.addCookie(sessionCookieRemove);
    }
}
