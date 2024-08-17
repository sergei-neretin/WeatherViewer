package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.time.ZonedDateTime;

@WebServlet("/login")
public class LoginController extends BaseController {
    private WebContext webContext;
    private final LoginService loginService = new LoginService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        webContext = Utils.buildWebContext(req, resp, getServletContext());
        templateEngine.process("login", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");
            SessionDto sessionDto = buildSessionDto(login, password);
            SessionDto session = loginService.login(sessionDto);
            Cookie cookie = new Cookie("sessionId", session.getId());
            cookie.setMaxAge(Integer.MAX_VALUE);
            resp.addCookie(cookie);
            resp.sendRedirect(getServletContext().getContextPath());
        } catch (UserNotFoundException e) {
            webContext = Utils.buildWebContext(req, resp, getServletContext());
            webContext.setVariable("userError", true);
            templateEngine.process("login", webContext, resp.getWriter());
        } catch (DatabaseException e) {
            webContext = Utils.buildWebContext(req, resp, getServletContext());
            webContext.setVariable("databaseError", true);
            templateEngine.process("login", webContext, resp.getWriter());
        }
    }

    private SessionDto buildSessionDto(String login, String password) {
        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .login(login)
                .password(password)
                .build();
        ZonedDateTime expiredTime = ZonedDateTime.now();
        expiredTime = expiredTime.plusHours(Utils.SESSION_TIME_IN_HOURS);
        return SessionDto.builder()
                .user(userDto)
                .expiresAt(expiredTime)
                .build();
    }
}
