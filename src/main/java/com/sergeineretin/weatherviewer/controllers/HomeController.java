package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.SessionCookieNotFoundException;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.service.LocationService;
import com.sergeineretin.weatherviewer.service.SessionService;
import com.sergeineretin.weatherviewer.service.OpenWeatherAPIService;
import com.sergeineretin.weatherviewer.service.WeatherService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

@WebServlet("")
public class HomeController extends BaseController {
    private final SessionService sessionService = SessionService.getInstance();
    private final LocationService locationService = LocationService.getInstance();
    private WeatherService weatherService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        this.templateEngine = Utils.buildTemplateEngine(servletContext);
        HttpClient client = HttpClient.newHttpClient();
        weatherService = new OpenWeatherAPIService(client);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            String sessionId = Utils.getSessionId(req);
            UserDto user = sessionService.getUserOrDeleteSession(sessionId);
            List<LocationWithTemperature> locationsWithTemperatures = locationService.findLocationsByUserId(user.getId());
            weatherService.updateTemperatures(locationsWithTemperatures);
            webContext.setVariable("user", user);
            webContext.setVariable("locationsWithTemperatures", locationsWithTemperatures);
            webContext.setVariable("authorized", true);
            templateEngine.process("homeSignedIn", webContext, resp.getWriter());
        } catch (SessionExpiredException e) {
            Utils.removeSessionIdCookie(resp);
            webContext.setVariable("authorized", false);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            webContext.setVariable("authorized", false);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sessionId = Utils.getSessionId(req);
            UserDto user = sessionService.getUserOrDeleteSession(sessionId);
            Long id = Long.parseLong(req.getParameter("id"));
            List<LocationWithTemperature> locationsByUserId = locationService.findLocationsByUserId(user.getId());
            for (LocationWithTemperature location : locationsByUserId) {
                if (location.getId().equals(id)) {
                    weatherService.deleteLocation(id);
                    break;
                }
            }
            resp.sendRedirect(getServletContext().getContextPath());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
