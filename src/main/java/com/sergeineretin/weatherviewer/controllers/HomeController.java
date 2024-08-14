package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.SessionCookieNotFoundException;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.service.SessionService;
import com.sergeineretin.weatherviewer.service.OpenWeatherAPIService;
import com.sergeineretin.weatherviewer.service.WeatherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.http.HttpClient;

@WebServlet("")
public class HomeController extends BaseController {
    private final SessionService service = SessionService.getInstance();
    private WeatherService weatherService;

    @Override
    public void init() throws ServletException {
        HttpClient client = HttpClient.newHttpClient();
        weatherService = new OpenWeatherAPIService(client);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            String sessionId = Utils.getSessionId(req);
            UserDto user = service.getUserOrDeleteSession(sessionId);
            weatherService.updateTemperatures(user.getLocations());
            webContext.setVariable("user", user);
            templateEngine.process("homeSignedIn", webContext, resp.getWriter());
        } catch (SessionExpiredException e) {
            Utils.removeSessionIdCookie(resp);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            String sessionId = Utils.getSessionId(req);
            UserDto user = service.getUserOrDeleteSession(sessionId);
            String id = req.getParameter("id");
            weatherService.deleteLocation(Long.parseLong(id));
            weatherService.updateTemperatures(user.getLocations());
            webContext.setVariable("user", user);
            templateEngine.process("homeSignedIn", webContext, resp.getWriter());
        } catch (SessionExpiredException e) {
            Utils.removeSessionIdCookie(resp);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }
}
