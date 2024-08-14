package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dto.LocationDto;

import java.math.BigDecimal;
import java.util.List;

public interface WeatherService {
    LocationDto findByName(String name);
    LocationDto findByCoordinates(BigDecimal latitude, BigDecimal longitude);
    void updateTemperatures(List<LocationDto> user);
    void deleteLocation(Long locationId);
    LocationDto addLocation(LocationDto locationDto);
}
