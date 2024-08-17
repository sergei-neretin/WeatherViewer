package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends BaseController {
    SessionService sessionService = SessionService.getInstance();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            String sessionId = Utils.getSessionId(req);
            Utils.removeSessionIdCookie(resp);
            sessionService.deleteSession(sessionId);
            resp.sendRedirect(getServletContext().getContextPath());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
