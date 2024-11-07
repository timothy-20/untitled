package com.timothy.services;

import com.timothy.entities.TKUserEntity;
import com.timothy.repositories.TKUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TKUserService {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private final TKUserRepository repository;

    public TKUserEntity getUserById(String id) {
        return this.repository.getUserById(id);
    }

    public List<TKUserEntity> getUsers() {
        return this.repository.getUsers();
    }

    public void insertUser(TKUserEntity user) {
        TKUserEntity existingUser = this.repository.getUserById(user.getId());

        if (existingUser != null) {
            LOGGER.warn("Fail to insert user. Because user already exists.");
        }

        this.repository.insertUser(user);
    }

    public void updateUser(TKUserEntity user) {
        this.repository.updateUser(user);
    }

    public void deleteUserById(String id) {
        TKUserEntity user = this.repository.getUserById(id);

        if (user == null) {
            LOGGER.warn("Fail to delete user by id: {}. Because no user has that id. ", id);
        }

        this.repository.deleteUserById(id);
    }
}
