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
        UserRegistrationDto user = TestUtils.getUser();
        SessionDto expiredSession = TestUtils.getExpiredSession();
        registrationService.register(user);
        SessionDto session = underTest.login(expiredSession);
        assertEquals(1L, session.getId());
    }
}
