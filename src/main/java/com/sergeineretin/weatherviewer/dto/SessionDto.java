package com.sergeineretin.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SessionDto {
    private String id;
    private UserRegistrationDto user;
    private ZonedDateTime expiresAt;
}
