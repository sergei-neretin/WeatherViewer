package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/register")
public class RegistrationController extends BaseController {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine.process("register", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String passwordAgain = req.getParameter("password_again");

        if (!password.equals(passwordAgain)) {
            webContext.setVariable("passwordsMatchError", true);
            templateEngine.process("register", webContext, resp.getWriter());
        } else if (password.length() < 6) {
            webContext.setVariable("passwordsTooShortError", true);
            templateEngine.process("register", webContext, resp.getWriter());
        } else {
            UserRegistrationDto userDto = UserRegistrationDto.builder().login(login).password(password).build();
            UserDto registeredUser = registrationService.register(userDto);
            webContext.setVariable("login", registeredUser.getLogin());
            resp.setStatus(HttpServletResponse.SC_OK);
            templateEngine.process("success", webContext, resp.getWriter());
        }
    }
}
