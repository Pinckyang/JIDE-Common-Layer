/*
 * @(#) DateConverter.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */
package com.jidesoft.converter;

import java.util.Calendar;
import java.util.Date;

/**
 * Converter which converts Calendar to String and converts it back.
 */
public class CalendarConverter extends DateConverter {

    /**
     * Creates a new CalendarConverter.
     */
    public CalendarConverter() {
    }

    public String toString(Object object, ConverterContext context) {
        if (object == null || !(object instanceof Calendar)) {
            return "";
        }
        else {
            return super.toString(((Calendar) object).getTime(), context);
        }
    }

    /**
     * Converts from a String to a Calendar.
     *
     * @param string  the string to be converted.
     * @param context the context. It could be DATETIME_CONTEXT, DATE_CONTEXT or TIME_CONTEXT.
     * @return the Calendar object. If the string is null or empty, null will be returned. If the string cannot be parsed as a date, the string itself will be returned.
     */
    public Object fromString(String string, ConverterContext context) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }

        Object date = super.fromString(string, context);
        Calendar calendar = Calendar.getInstance();
        if (date instanceof Date) {
            calendar.setTime((Date) date);
            return calendar;
        }
        else {
            return string;
        }
    }
}