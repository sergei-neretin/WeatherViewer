package com.sergeineretin.weatherviewer.dto;

import com.sergeineretin.weatherviewer.model.User;
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
    private Long id;
    private User user;
    private ZonedDateTime session;
}
