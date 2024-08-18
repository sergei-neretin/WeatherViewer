package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.dao.impl.LocationDaoImpl;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.exceptions.LocationException;
import com.sergeineretin.weatherviewer.exceptions.ServerException;
import com.sergeineretin.weatherviewer.exceptions.UnexpectedException;
import com.sergeineretin.weatherviewer.model.Location;
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
import java.util.List;

@Slf4j
public class OpenWeatherAPIService implements WeatherService {
    private final HttpClient httpClient;
    private final LocationDao locationDao = new LocationDaoImpl();
    public OpenWeatherAPIService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void updateTemperatures(List<LocationDto> locations) {
        for(LocationDto location : locations) {
            BigDecimal temperature = findByCoordinates(location.getLatitude(), location.getLongitude()).getTemperature();
            location.setTemperature(temperature);
        }
    }

    @Override
    public void deleteLocation(Long locationId) {
        locationDao.deleteById(locationId);
    }

    public LocationDto addLocation(LocationDto locationDto) {
        ModelMapper modelMapper = new ModelMapper();
        Location location = modelMapper.map(locationDto, Location.class);
        locationDao.save(location);
        return modelMapper.map(location, LocationDto.class);
    }

    @Override
    public LocationDto findByName(String name) throws LocationException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&appid=" + Utils.API_KEY))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return getLocation(request);
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            throw new UnexpectedException(e.getMessage());
        }
    }

    @Override
    public LocationDto findByCoordinates(BigDecimal latitude, BigDecimal longitude) throws LocationException {
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

    private LocationDto getLocation(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String body = response.body();
                log.info(body);
                LocationDto locationDto = Utils.getMapper().readValue(body, LocationDto.class);
                BigDecimal tempInCelsius = locationDto.getTemperature().add(new BigDecimal(-Utils.ZERO_CELSIUS_IN_KELVINS));
                locationDto.setTemperature(tempInCelsius);
                return locationDto;
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
