package com.timothy.services;

import com.timothy.models.TKVariety;
import com.timothy.repositories.TKVarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TKVarietyService {
    private final TKVarietyRepository repository;

    @Autowired
    public TKVarietyService(TKVarietyRepository varietyRepository) {
        super();

        this.repository = varietyRepository;
    }

    public List<TKVariety> findAll() {
        return this.repository.findAll();
    }

    public TKVariety findById(int id) {
        return this.repository.findById(id);
    }
}
