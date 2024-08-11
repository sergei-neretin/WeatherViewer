package com.sergeineretin.weatherviewer.controllers;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import com.sergeineretin.weatherviewer.service.HomeService;
import com.sergeineretin.weatherviewer.service.OpenWeatherAPIService;
import com.sergeineretin.weatherviewer.service.WeatherAPIService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("")
public class HomeController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = Utils.buildWebContext(req, resp, getServletContext());
        HomeService service = new HomeService();
        HttpClient client = HttpClient.newHttpClient();
        WeatherAPIService weatherAPIService = new OpenWeatherAPIService(client);
        try {
            if (req.getCookies() != null) {
                Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                        .filter(c -> c.getName().equals("sessionId"))
                        .findFirst();

                if (cookie.isPresent()) {
                    String sessionId = cookie.get().getValue();
                    System.out.println(sessionId);
                    SessionDto session = service.getSessionOrDelete(sessionId);
                    LocationDto novosibirsk = weatherAPIService.findByName("Novosibirsk");
                    webContext.setVariable("login", session.getUser().getLogin());
                    webContext.setVariable("name", novosibirsk.getName());
                    webContext.setVariable("temp", novosibirsk.getTemperature());
                    webContext.setVariable("lat", novosibirsk.getLatitude());
                    webContext.setVariable("lon", novosibirsk.getLongitude());
                    templateEngine.process("homeSignedIn", webContext, resp.getWriter());
                } else {
                    templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
                }
            } else {
                templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
            }
        } catch (SessionExpiredException e) {
            webContext.setVariable("sessionExpired", true);
            templateEngine.process("homeNotSignedIn", webContext, resp.getWriter());
        }
    }
}
