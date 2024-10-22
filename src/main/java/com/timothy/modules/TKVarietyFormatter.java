package com.timothy.modules;

import com.timothy.models.TKVariety;
import com.timothy.services.TKVarietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class TKVarietyFormatter implements Formatter<TKVariety> {
    private final TKVarietyService service;

    @Autowired
    public TKVarietyFormatter(TKVarietyService varietyService) {
        super();

        this.service = varietyService;
    }

    @Override
    public TKVariety parse(String text, Locale locale) throws ParseException {
        final int varietyId = Integer.parseInt(text);
        return this.service.findById(varietyId);
    }

    @Override
    public String print(TKVariety object, Locale locale) {
        return object.getId().toString();
    }
}
