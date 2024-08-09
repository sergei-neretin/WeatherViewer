package com.sergeineretin.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRegistrationDto {
    private Long id;
    private String login;
    private String password;
}
