package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.exceptions.SessionExpiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HomeServiceIntegrationTests {
    RegistrationService registrationService = new RegistrationService();
    LoginService loginService = new LoginService();
    HomeService underTest = new HomeService();

    @Test
    public void testThatHomeServiceGetSessionOrDeleteReturnsSessionById() {
        UserRegistrationDto user = TestUtils.getUser();
        SessionDto session = TestUtils.getSession();
        registrationService.register(user);
        Long sessionId = loginService.login(session).getId();
        SessionDto result = underTest.getSessionOrDelete(sessionId);
        assertEquals(user.getLogin(), result.getUser().getLogin());
    }

    @Test
    public void testThatHomeServiceGetSessionOrDeleteThrowsExceptionWhenSessionIsExpired() {
        UserRegistrationDto user = TestUtils.getUser();
        SessionDto expiredSession = TestUtils.getExpiredSession();
        registrationService.register(user);
        Long sessionId = loginService.login(expiredSession).getId();
        assertThrows(SessionExpiredException.class, () -> underTest.getSessionOrDelete(sessionId));
    }
}
