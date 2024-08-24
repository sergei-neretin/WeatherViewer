package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends BaseController {
    private final LoginService loginService = new LoginService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        webContext = Utils.buildWebContext(req, resp, getServletContext());
        templateEngine.process("login", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        SessionDto session = loginService.login(login, password);
        Cookie cookie = new Cookie("sessionId", session.getId());
        cookie.setMaxAge(Integer.MAX_VALUE);
        resp.addCookie(cookie);
        resp.sendRedirect(getServletContext().getContextPath());
    }
}
