package com.sergeineretin.weatherviewer.dao.impl;

import com.sergeineretin.weatherviewer.dao.UserDao;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.exceptions.UniqueConstraintViolationException;
import com.sergeineretin.weatherviewer.model.User;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

public class UserDaoImpl implements UserDao {
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new DatabaseException("Database unavailable");
        }
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
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
