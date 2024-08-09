package com.sergeineretin.weatherviewer.dao.impl;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.UniqueConstraintViolationException;
import com.sergeineretin.weatherviewer.model.User;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

public class UserDaoImpl implements UserDao {


    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User save(User user) {
        Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new UniqueConstraintViolationException("User with login " + user.getLogin() + " already exists");
            } else {
                throw new DatabaseException("Database unavailable");
            }
        } finally {
            session.close();
        }
    }
}
