package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.exceptions.LocationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenWeatherAPIServiceTests {
    @Test
    public void testOpenWeatherAPIServiceFindByName() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        var mockedHttpResponse = mock(HttpResponse.class);
        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON);
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
        when(mockedHttpResponse.body()).thenReturn(TestUtils.OPEN_WEATHER_API_JSON);
        when(mockedHttpClient.send(any(), any())).thenReturn(mockedHttpResponse);
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        LocationDto novosibirsk = underTest.findByCoordinates(new BigDecimal("55.0328"), new BigDecimal("82.9282"));

        assertEquals("Novosibirsk", novosibirsk.getName());
        assertEquals(new BigDecimal("55.0328"), novosibirsk.getLatitude());
        assertEquals(new BigDecimal("82.9282"), novosibirsk.getLongitude());
        assertEquals(new BigDecimal("290.84"), novosibirsk.getTemperature());
    }

    @Test
    public void testOpenWeatherAPIServiceThrowsException() throws IOException, InterruptedException {
        HttpClient mockedHttpClient = mock(HttpClient.class);
        when(mockedHttpClient.send(any(), any())).thenThrow(new IOException());
        OpenWeatherAPIService underTest = new OpenWeatherAPIService(mockedHttpClient);

        assertThrows(LocationException.class, () -> underTest.findByName(""));
    }
}
