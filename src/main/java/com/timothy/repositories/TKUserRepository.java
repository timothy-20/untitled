package com.timothy.repositories;

import com.timothy.entities.TKUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TKUserRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TKUserRepository(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
    }

    public TKUserEntity getUserById(String id) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(TKUserEntity.class, id);
    }

    public List<TKUserEntity> getUsers() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery("from TKUserEntity", TKUserEntity.class).list();
    }

    public void insertUser(TKUserEntity user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
    }

    public void updateUser(TKUserEntity user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.merge(user);
    }

    public void deleteUser(String id) {
        Session session = this.sessionFactory.getCurrentSession();
        TKUserEntity targetUser = session.get(TKUserEntity.class, id);

        if (targetUser != null) {
            session.remove(targetUser);
        }
    }
}
