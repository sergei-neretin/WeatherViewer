package com.sergeineretin.weatherviewer.dto;

import com.sergeineretin.weatherviewer.model.User;
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
    private UserRegistrationDto user;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
