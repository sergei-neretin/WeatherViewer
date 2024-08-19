package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.model.LocationApiResponse;

import java.math.BigDecimal;
import java.util.List;

public interface WeatherService {
    List<LocationApiResponse> findByName(String name);
    LocationWithTemperature findByCoordinates(BigDecimal latitude, BigDecimal longitude);
    void updateTemperatures(List<LocationWithTemperature> user);
    void deleteLocation(Long locationId);
    LocationWithTemperature addLocation(LocationWithTemperature locationWithTemperature);
}
