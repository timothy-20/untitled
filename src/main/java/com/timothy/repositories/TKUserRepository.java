package com.timothy.repositories;

import com.timothy.entities.TKUserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TKUserRepository {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private final SessionFactory sessionFactory;

    public TKUserEntity getUserById(String id) {
        LOGGER.debug("Get user by id: {}", id);
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(TKUserEntity.class, id);
    }

    public List<TKUserEntity> getUsers() {
        LOGGER.debug("Get users");
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from TKUserEntity", TKUserEntity.class).getResultList();
    }

    public void insertUser(TKUserEntity user) {
        LOGGER.debug("Insert user: {}", user);
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
    }

    public void updateUser(TKUserEntity user) {
        LOGGER.debug("Update user: {}", user);
        Session session = this.sessionFactory.getCurrentSession();
        session.merge(user);
    }

    public void deleteUserById(String id) {
        LOGGER.debug("Delete user by id: {}", id);
        Session session = this.sessionFactory.getCurrentSession();
        TKUserEntity targetUser = session.find(TKUserEntity.class, id);
        session.remove(targetUser);
    }
}