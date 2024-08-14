package com.sergeineretin.weatherviewer.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String login;
    private List<LocationDto> locations;
}
