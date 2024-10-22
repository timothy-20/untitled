package com.timothy.repositories;

import com.timothy.models.TKVariety;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TKVarietyRepository {
    private final Map<Integer, TKVariety> varietiesById;

    public TKVarietyRepository() {
        super();

        this.varietiesById = new LinkedHashMap<>();

        final TKVariety var1 = new TKVariety(1, "timothy");
        this.varietiesById.put(var1.getId(), var1);

        final TKVariety var2 = new TKVariety(2, "peco");
        this.varietiesById.put(var2.getId(), var2);

        final TKVariety var3 = new TKVariety(3, "lay");
        this.varietiesById.put(var3.getId(), var3);
    }

    public List<TKVariety> findAll() {
        return new ArrayList<>(this.varietiesById.values());
    }

    public TKVariety findById(int id) {
        return this.varietiesById.get(id);
    }
}
