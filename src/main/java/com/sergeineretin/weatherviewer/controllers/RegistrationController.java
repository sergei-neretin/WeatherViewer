package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.UniqueConstraintViolationException;
import com.sergeineretin.weatherviewer.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@Slf4j
@WebServlet("/register")
public class RegistrationController extends BaseController {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        templateEngine.process("register", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
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
                UserDto userDto = UserDto.builder().login(login).password(password).build();
                registrationService.register(userDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(req.getContextPath() + "/success.html");
            }
        } catch (DatabaseException e) {
            log.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (UniqueConstraintViolationException e) {
            log.info(e.getMessage());
            webContext.setVariable("loginAlreadyExist", true);
            templateEngine.process("register", webContext, resp.getWriter());
        }
    }
}
