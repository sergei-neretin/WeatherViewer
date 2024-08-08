package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends BaseController {
    private WebContext webContext;
    private LoginService loginService = new LoginService();
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
            UserDto userDto = UserDto.builder()
                    .login(login)
                    .password(password)
                    .build();
            SessionDto session = loginService.login(userDto);
            System.out.println(session.getId());
            System.out.println(session.getUser());
            System.out.println(session.getExpiresAt());
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
}
