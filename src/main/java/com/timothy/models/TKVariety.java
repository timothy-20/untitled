package com.timothy.models;

import lombok.Getter;
import lombok.Setter;

public class TKVariety {
    @Getter
    private final Integer id;
    @Setter
    private String name;

    public TKVariety(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}
