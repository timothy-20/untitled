package com.timothy.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TKDateFormatter implements Formatter<Date> {
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    private final MessageSource messageSource;

    @Autowired
    public TKDateFormatter(MessageSource messageSource) {
        super();

        this.messageSource = messageSource;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        final SimpleDateFormat formatter = this.createDateFormat(locale);
        return formatter.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        final SimpleDateFormat formatter = this.createDateFormat(locale);
        return formatter.format(object);
    }

    private SimpleDateFormat createDateFormat(final Locale locale) {
        final String format = this.messageSource.getMessage(DEFAULT_DATE_FORMAT, null, locale);
        final SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        formatter.setLenient(false);
        return formatter;
    }
}
