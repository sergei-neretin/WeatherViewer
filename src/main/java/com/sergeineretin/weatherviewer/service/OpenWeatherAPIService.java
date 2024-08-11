package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.exceptions.LocationException;
import com.sergeineretin.weatherviewer.exceptions.UnexpectedException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OpenWeatherAPIService implements WeatherAPIService {
    private final HttpClient httpClient;
    public OpenWeatherAPIService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    @Override
    public LocationDto findByName(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&appid=" + Utils.API_KEY))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return getLocation(request);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new LocationException("An error occurs when sending or receiving location data");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }
    @Override
    public LocationDto findByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openweathermap.org/data/2.5/weather?lon=" + longitude.toString() + "&lat=" + latitude.toString() + "&appid=" + Utils.API_KEY))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return getLocation(request);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new LocationException("An error occurs when sending or receiving location data");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("");
        }
    }

    private LocationDto getLocation(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        return Utils.getMapper().readValue(body, LocationDto.class);
    }

}
