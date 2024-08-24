package com.sergeineretin.weatherviewer.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.dao.impl.SessionDaoImpl;
import com.sergeineretin.weatherviewer.dao.impl.UserDaoImpl;
import com.sergeineretin.weatherviewer.dto.SessionDto;
import com.sergeineretin.weatherviewer.exceptions.UserNotFoundException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;

public class LoginService {
    SessionDao sessionDao = new SessionDaoImpl();
    UserDao userDao = new UserDaoImpl();
    ModelMapper modelMapper = new ModelMapper();

    public SessionDto login(String login, String password) {
        User user = userDao.getUserByLogin(login);
        String encryptedPassword = user.getPassword();
        if (BCrypt.verifyer().verify(password.toCharArray(),  encryptedPassword).verified) {
            Session session = Session.builder()
                    .expiresAt(ZonedDateTime.now().plusHours(6))
                    .user(user)
                    .build();
            sessionDao.saveSession(session);
            return modelMapper.map(session, SessionDto.class);
        } else {
            throw new UserNotFoundException("Invalid username or password");
        }
    }
}
