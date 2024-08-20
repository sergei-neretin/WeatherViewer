package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.LocationDaoImpl;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.model.LocationWithTemperature;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.model.Location;
import com.sergeineretin.weatherviewer.model.LocationApiResponse;
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

        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_LOCATIONS_JSON);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        List<LocationApiResponse> novosibirsk = underTest.findByName("Novosibirsk");

        assertEquals("Novosibirsk", novosibirsk.get(0).getName());
    }

    @Test
    public void testOpenWeatherAPIServiceFindByCoordinates() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.statusCode()).thenReturn(200);

        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON1);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        LocationWithTemperature novosibirsk = underTest.findByCoordinates(new BigDecimal("55.0328"), new BigDecimal("82.9282"));

        assertEquals("Novosibirsk", novosibirsk.getName());
        assertEquals(new BigDecimal("17.84"), novosibirsk.getMain().getTemperature());
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
        LocationWithTemperature location = TestUtils.getLocation1();
        location.setUser(register);

        LocationWithTemperature locationWithTemperature = underTest.addLocation(location);
        List<Location> locations = new ArrayList<>(locationDao.findByUserId(register.getId()));
        assertEquals(1, locations.size());
        assertEquals(locations.get(0).getId(), locationWithTemperature.getId());
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

        LocationWithTemperature location1 = TestUtils.getLocation1();
        location1.setUser(register);
        LocationWithTemperature locationDto1 = underTest.addLocation(location1);

        underTest.deleteLocation(locationDto1.getId());
        List<Location> result = locationDao.findByUserId(register.getId());
        assertTrue(result.isEmpty());
    }
}
