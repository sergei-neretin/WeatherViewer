package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginServiceIntegrationTests {
    RegistrationService registrationService = new RegistrationService();
    LoginService underTest = new LoginService();

    @Test
    public void testThatLoginServiceLoginReturnsSessionWithIdOfRegisteredUser() {
        UserRegistrationDto user = TestUtils.getUser1();
        SessionDto sessionDto = TestUtils.getSession();

        registrationService.register(user);
        SessionDto result = underTest.login(sessionDto);

        assertEquals(sessionDto.getUser().getLogin(), result.getUser().getLogin());
    }
}
