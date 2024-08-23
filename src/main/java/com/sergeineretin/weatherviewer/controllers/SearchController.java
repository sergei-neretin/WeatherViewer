package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.model.LocationApiResponse;
import com.sergeineretin.weatherviewer.service.OpenWeatherAPIService;
import com.sergeineretin.weatherviewer.service.SessionService;
import com.sergeineretin.weatherviewer.service.WeatherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        super.init();
        HttpClient client = HttpClient.newHttpClient();
        weatherService = new OpenWeatherAPIService(client);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = getSessionId(req);
        UserDto userDto = service.getUserOrDeleteSession(sessionId);
        String name = req.getParameter("name");
        List<LocationApiResponse> locations = weatherService.findByName(name);
        webContext.setVariable("name", name);
        webContext.setVariable("locations", locations);
        webContext.setVariable("user", userDto);
        webContext.setVariable("authorized", true);
        templateEngine.process("search", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = getSessionId(req);
        UserDto userDto = service.getUserOrDeleteSession(sessionId);
        LocationWithTemperature location = getLocation(req);
        location.setUser(userDto);
        weatherService.addLocation(location);
        resp.sendRedirect(req.getContextPath() + "/");
    }

    private LocationWithTemperature getLocation(HttpServletRequest req) {
        String name = req.getParameter("name");
        BigDecimal lon = new BigDecimal(req.getParameter("lon"));
        BigDecimal lat = new BigDecimal(req.getParameter("lat"));
        return LocationWithTemperature.builder()
                .name(name)
                .longitude(lon)
                .latitude(lat)
                .build();
    }
}
