package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.exceptions.SessionCookieNotFoundException;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.model.LocationApiResponse;
import com.sergeineretin.weatherviewer.service.OpenWeatherAPIService;
import com.sergeineretin.weatherviewer.service.SessionService;
import com.sergeineretin.weatherviewer.service.WeatherService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.List;

@WebServlet("/search")
public class SearchController extends BaseController {
    private WeatherService weatherService;
    private final SessionService service = SessionService.getInstance();

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
            UserDto userDto = service.getUserOrDeleteSession(sessionId);
            String name = req.getParameter("name");
            List<LocationApiResponse> locations = weatherService.findByName(name);
            webContext.setVariable("locations", locations);
            webContext.setVariable("user", userDto);
            webContext.setVariable("authorized", true);
            templateEngine.process("search", webContext, resp.getWriter());
        } catch (SessionExpiredException e) {
            Utils.removeSessionIdCookie(resp);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        try {
            String sessionId = Utils.getSessionId(req);
            UserDto userDto = service.getUserOrDeleteSession(sessionId);
            LocationWithTemperature location = getLocation(req);
            location.setUser(userDto);
            weatherService.addLocation(location);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (SessionExpiredException e) {
            Utils.removeSessionIdCookie(resp);
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        } catch (SessionCookieNotFoundException e) {
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }

    private LocationWithTemperature getLocation(HttpServletRequest req) {
        String name = (String) req.getParameter("name");
        BigDecimal lon = new BigDecimal((String) req.getParameter("lon"));
        BigDecimal lat = new BigDecimal((String) req.getParameter("lat"));
        return LocationWithTemperature.builder()
                .name(name)
                .longitude(lon)
                .latitude(lat)
                .build();
    }
}
