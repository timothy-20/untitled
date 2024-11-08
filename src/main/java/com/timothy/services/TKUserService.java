package com.timothy.services;

import com.timothy.entities.TKUserEntity;
import com.timothy.repositories.TKUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TKUserService {
    private final TKUserRepository repository;

    public TKUserEntity getUserById(String id) {
        return this.repository.getUserById(id);
    }

    public List<TKUserEntity> getUsers() {
        return this.repository.getUsers();
    }

    public void insertUser(TKUserEntity user) {
        this.repository.insertUser(user);
    }

    public void updateUser(TKUserEntity user) {
        this.repository.updateUser(user);
    }

    public void deleteUserById(String id) {
        this.repository.deleteUserById(id);
    }
}
