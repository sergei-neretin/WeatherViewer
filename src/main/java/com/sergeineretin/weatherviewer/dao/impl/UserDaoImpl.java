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
    public User getUserById(long id) {
        Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            throw new DatabaseException("Database unavailable");
        } finally {
            session.close();
        }
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

    @Override
    public void deleteUserById(long id) {
        try (Session session = Utils.getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new DatabaseException("Database unavailable");
        }
    }
}
