package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginServiceIntegrationTests {
    LoginService underTest = new LoginService();

    @Test
    public void testThatLoginServiceLoginReturnsSessionWithIdOfRegisteredUser() {
        UserRegistrationDto userRegistrationDto = TestUtils.getUser1Dto();
        RegistrationService registrationService = new RegistrationService();
        registrationService.register(userRegistrationDto);
        SessionDto sessionDto = TestUtils.getSessionDto();
        sessionDto.setUser(userRegistrationDto);
        SessionDto result = underTest.login("John Doe", "password1");

        assertEquals(userRegistrationDto.getLogin(), result.getUser().getLogin());
    }
}
