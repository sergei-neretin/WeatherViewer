package com.sergeineretin.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationDto {
    private Long id;
    private String name;
    private UserDto user;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal temperature;
}
