package com.timothy.services;

import com.timothy.entities.TKUserEntity;
import com.timothy.repositories.TKUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TKUserService {
    private final TKUserRepository repository;

    @Autowired
    public TKUserService(TKUserRepository repository) {
        super();
        this.repository = repository;
    }

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

    public void deleteUser(String id) {
        this.repository.deleteUser(id);
    }
}
