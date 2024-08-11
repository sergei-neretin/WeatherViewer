package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dto.LocationDto;

import java.math.BigDecimal;

public interface WeatherAPIService {
    LocationDto findByName(String name);
    LocationDto findByCoordinates(BigDecimal latitude, BigDecimal longitude);
}
