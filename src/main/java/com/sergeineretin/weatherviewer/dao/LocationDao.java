package com.sergeineretin.weatherviewer.dao;

import com.sergeineretin.weatherviewer.model.Location;

import java.util.List;

public interface LocationDao {
    List<Location> findByUserId(Long userId);
    void save(Location location);
    void deleteById(Long id);
}
