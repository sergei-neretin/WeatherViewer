package com.sergeineretin.weatherviewer.dao.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.SessionDao;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.model.Session;
import com.sergeineretin.weatherviewer.model.User;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SessionDaoImpl implements SessionDao {
    @Override
    public void saveSession(Session userSession) {
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            User user = session.createQuery("from User where id = :id", User.class)
                    .setParameter("id", userSession.getUser().getId())
                    .getSingleResult();
            userSession.setUser(user);
            session.persist(userSession);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            log.info("Error creating session", e);
            throw new DatabaseException("Database error");
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Session> findById(String sessionId) {
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Session result = session.get(Session.class, sessionId);
            if (result.getExpiresAt().isAfter(ZonedDateTime.now())) {
                session.getTransaction().commit();
                return Optional.of(result);
            } else {
                HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
                JpaCriteriaDelete<Session> criteriaDelete = cb.createCriteriaDelete(Session.class);
                Root<Session> root = criteriaDelete.from(Session.class);
                criteriaDelete.where(cb.equal(root.get("id"), sessionId));
                session.createMutationQuery(criteriaDelete).executeUpdate();
                session.getTransaction().commit();
                return Optional.empty();
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("Database error");
        } finally {
            session.close();
        }
    }

    @Override
    public List<Session> findAllSessions() {
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
             session.beginTransaction();
            List<Session> fromSession = session.createQuery("from Session", Session.class).list();
            session.getTransaction().commit();
            return fromSession;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("Database error");
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(String sessionId) {
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Session result = session.get(Session.class, sessionId);
            session.detach(result);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("Database error");
        } finally {
            session.close();
        }
    }
}
