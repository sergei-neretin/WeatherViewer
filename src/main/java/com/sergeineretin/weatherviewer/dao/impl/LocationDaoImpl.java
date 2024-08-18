package com.sergeineretin.weatherviewer.dao.impl;

import com.sergeineretin.weatherviewer.Utils;
import com.sergeineretin.weatherviewer.dao.LocationDao;
import com.sergeineretin.weatherviewer.exceptions.DatabaseException;
import com.sergeineretin.weatherviewer.model.Location;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaDelete;

import java.util.List;

public class LocationDaoImpl implements LocationDao {

    @Override
    public List<Location> findByUserId(Long userId) {
        org.hibernate.Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            List<Location> locations = session.createQuery("from Location where user.id = :userId", Location.class)
                    .setParameter("userId", userId).list();
            session.getTransaction().commit();
            return locations;
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("database exception");
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Location location) {
        Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();;
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("database exception");
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = Utils.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaDelete<Location> criteriaDelete = cb.createCriteriaDelete(Location.class);
            Root<Location> root = criteriaDelete.from(Location.class);
            criteriaDelete.where(cb.equal(root.get("id"), id));
            session.createMutationQuery(criteriaDelete).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DatabaseException("database exception");
        } finally {
            session.close();
        }
    }
}
