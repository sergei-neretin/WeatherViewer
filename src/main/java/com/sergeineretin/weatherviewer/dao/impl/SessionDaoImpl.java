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
import java.util.Optional;

@Slf4j
public class SessionDaoImpl implements SessionDao {
    @Override
    public Optional<Session> createSession(Session userSession) {
        User user = userSession.getUser();
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<User> cr = cb.createQuery(User.class);
            JpaRoot<User> root = cr.from(User.class);

            JpaPredicate login = cb.equal(root.get("login"), user.getLogin());
            cr.select(root).where(login);

            Query<User> query = session.createQuery(cr);
            User singleResult = query.getSingleResult();
            String  bcryptHashString = singleResult.getPassword();
            String password = userSession.getUser().getPassword();
            if (BCrypt.verifyer().verify(password.toCharArray(),  bcryptHashString).verified) {
                userSession.setUser(singleResult);
                session.persist(userSession);
                session.getTransaction().commit();

                return Optional.of(userSession);
            } else {
                return Optional.empty();
            }
        } catch (NoResultException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            return Optional.empty();
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
