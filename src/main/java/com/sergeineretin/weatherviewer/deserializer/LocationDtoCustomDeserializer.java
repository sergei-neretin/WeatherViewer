package com.sergeineretin.weatherviewer.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.sergeineretin.weatherviewer.dto.LocationDto;
import com.sergeineretin.weatherviewer.exceptions.UnexpectedException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
public class LocationDtoCustomDeserializer extends StdDeserializer<LocationDto> {
    public LocationDtoCustomDeserializer() {
        this(null);
    }

    public LocationDtoCustomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocationDto deserialize(JsonParser parser, DeserializationContext deserializer) {
        try {
            LocationDto locationDto = new LocationDto();
            ObjectCodec codec = parser.getCodec();
            JsonNode root = codec.readTree(parser);

            JsonNode coordNode = root.path("coord");
            BigDecimal latitude = coordNode.has("lat") ? new BigDecimal(coordNode.get("lat").asText()) : null;
            BigDecimal longitude = coordNode.has("lon") ? new BigDecimal(coordNode.get("lon").asText()) : null;

            String name = root.path("name").asText();
            BigDecimal temperature = root.path("main").path("temp").asText().isEmpty() ? null : new BigDecimal(root.path("main").path("temp").asText());


            locationDto.setLatitude(latitude);
            locationDto.setLongitude(longitude);
            locationDto.setName(name);
            locationDto.setTemperature(temperature);

            return locationDto;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UnexpectedException("Unexpected error occurred while deserializing location: " + e.getMessage());
        }
    }
}
