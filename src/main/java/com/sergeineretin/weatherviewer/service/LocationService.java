package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.dao.impl.LocationDaoImpl;
import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.model.Location;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class LocationService {
    private static LocationService instance;
    private final LocationDao locationDao = new LocationDaoImpl();
    private LocationService() { }
    public static synchronized LocationService getInstance() {
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }
    public List<LocationWithTemperature> findLocationsByUserId(long userId) {
        ModelMapper modelMapper = new ModelMapper();
        List<Location> locations = locationDao.findByUserId(userId);
        return locations.stream()
                .map( location -> modelMapper.map(location, LocationWithTemperature.class))
                .collect(Collectors.toList());
    }
}
