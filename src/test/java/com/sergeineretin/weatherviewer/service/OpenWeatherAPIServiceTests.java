package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.LocationDaoImpl;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.Location;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenWeatherAPIServiceTests {
    RegistrationService registrationService = new RegistrationService();
    LocationDao locationDao = new LocationDaoImpl();
    UserDao userDao = new UserDaoImpl();

    @Test
    public void testOpenWeatherAPIServiceFindByName() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.statusCode()).thenReturn(200);

        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON1);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        LocationDto novosibirsk = underTest.findByName("Novosibirsk");

        assertEquals("Novosibirsk", novosibirsk.getName());
        assertEquals(new BigDecimal("55.0328"), novosibirsk.getLatitude());
        assertEquals(new BigDecimal("82.9282"), novosibirsk.getLongitude());
        assertEquals(new BigDecimal("290.84"), novosibirsk.getTemperature());
    }

    @Test
    public void testOpenWeatherAPIServiceFindByCoordinates() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.statusCode()).thenReturn(200);

        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON1);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        LocationDto novosibirsk = underTest.findByCoordinates(new BigDecimal("55.0328"), new BigDecimal("82.9282"));

        assertEquals("Novosibirsk", novosibirsk.getName());
        assertEquals(new BigDecimal("55.0328"), novosibirsk.getLatitude());
        assertEquals(new BigDecimal("82.9282"), novosibirsk.getLongitude());
        assertEquals(new BigDecimal("290.84"), novosibirsk.getTemperature());
    }

    @Test
    public void testOpenWeatherAPIServiceSave() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.statusCode()).thenReturn(200);
        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON1);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);
        UserRegistrationDto user = TestUtils.getUser1Dto();
        UserDto register = registrationService.register(user);
        LocationDto location = TestUtils.getLocation1();
        location.setUser(register);

        LocationDto locationDto = underTest.addLocation(location);
        List<Location> locations = new ArrayList<>(locationDao.findByUserId(register.getId()));
        assertEquals(1, locations.size());
        assertEquals(locations.get(0).getId(), locationDto.getId());
    }



    @Test
    public void testOpenWeatherAPIServiceDelete() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.statusCode()).thenReturn(200);
        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON1);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);
        UserRegistrationDto user = TestUtils.getUser2Dto();
        UserDto register = registrationService.register(user);

        LocationDto location1 = TestUtils.getLocation1();
        location1.setUser(register);
        LocationDto locationDto1 = underTest.addLocation(location1);

        underTest.deleteLocation(locationDto1.getId());
        List<Location> result = userDao.getUserById(register.getId()).getLocations();
        assertTrue(result.isEmpty());
    }
}
