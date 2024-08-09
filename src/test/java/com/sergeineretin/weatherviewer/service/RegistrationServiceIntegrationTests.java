package com.sergeineretin.weatherviewer.service;

import com.sergeineretin.weatherviewer.TestUtils;
import com.sergeineretin.weatherviewer.dto.UserDto;
import com.sergeineretin.weatherviewer.dto.UserRegistrationDto;
import com.sergeineretin.weatherviewer.exceptions.UniqueConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationServiceIntegrationTests {
    RegistrationService underTest = new RegistrationService();
    @Test
    public void testThatRegistrationServiceRegisterUserWithUniqueLoginReturnsCorrectId() {
        UserRegistrationDto userDto = TestUtils.getUser();
        UserDto result = underTest.register(userDto);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testThatRegistrationServiceRegisterUserWithNotUniqueLoginThrowsException() {
        UserRegistrationDto userDto = TestUtils.getUser();
        underTest.register(userDto);
        assertThrows(UniqueConstraintViolationException.class, () -> underTest.register(userDto));
    }
}
