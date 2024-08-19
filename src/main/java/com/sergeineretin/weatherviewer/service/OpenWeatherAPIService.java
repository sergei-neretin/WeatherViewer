package com.sergeineretin.weatherviewer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.dao.impl.LocationDaoImpl;
import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.exceptions.LocationException;
import com.sergeineretin.weatherviewer.exceptions.ServerException;
import com.sergeineretin.weatherviewer.exceptions.UnexpectedException;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.LocationApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OpenWeatherAPIService implements WeatherService {
    private final HttpClient httpClient;
    private final LocationDao locationDao = new LocationDaoImpl();
    public OpenWeatherAPIService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void updateTemperatures(List<LocationWithTemperature> locations) {
        for(LocationWithTemperature location : locations) {
            BigDecimal temperature = findByCoordinates(location.getLatitude(), location.getLongitude()).getMain().getTemperature();
            location.getMain().setTemperature(temperature);
        }
    }

    @Override
    public void deleteLocation(Long locationId) {
        locationDao.deleteById(locationId);
    }

    public LocationWithTemperature addLocation(LocationWithTemperature locationWithTemperature) {
        ModelMapper modelMapper = new ModelMapper();
        Location location = modelMapper.map(locationWithTemperature, Location.class);
        locationDao.save(location);
        return modelMapper.map(location, LocationWithTemperature.class);
    }

    @Override
    public List<LocationApiResponse> findByName(String name) throws LocationException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openweathermap.org/geo/1.0/direct?"
                            + "q=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                            + "&appid=" + Utils.API_KEY
                            + "&units=" + "metric"
                            + "&limit=6"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return getLocations(request);
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }

    private List<LocationApiResponse> getLocations(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                log.info(body);
                List<LocationApiResponse> locationDtos = Utils.getMapper().readValue(
                        body,
                        new TypeReference<>() {
                        }
                );
                return locationDtos;
            } else if (response.statusCode() == 404) {
                return new ArrayList<>();
            } else {
                throw new ServerException("Internal Server Error");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }

    @Override
    public LocationWithTemperature findByCoordinates(BigDecimal latitude, BigDecimal longitude) throws LocationException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openweathermap.org/data/2.5/weather?lon=" + longitude.toString() + "&lat=" + latitude.toString() + "&appid=" + Utils.API_KEY))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return getLocation(request);
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }

    private LocationWithTemperature getLocation(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                log.info(body);
                LocationWithTemperature locationWithTemperature = Utils.getMapper().readValue(body, LocationWithTemperature.class);
                BigDecimal tempInCelsius = locationWithTemperature.getMain().getTemperature().add(new BigDecimal(-Utils.ZERO_CELSIUS_IN_KELVINS));
                locationWithTemperature.getMain().setTemperature(tempInCelsius);
                return locationWithTemperature;
            } else if (response.statusCode() == 404) {
                throw new LocationException("No weather data found");
            } else {
                throw new ServerException("Internal Server Error");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }

}
