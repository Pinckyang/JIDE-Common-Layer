/*
 * @(#)DoubleConverter.java 3/9/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */
package com.jidesoft.converter;

import java.text.NumberFormat;
import java.text.ParseException;


/**
 * Converter which converts Double to String and converts it back.
 */
public class DoubleConverter extends NumberConverter {
    public DoubleConverter() {
    }

    public DoubleConverter(NumberFormat format) {
        super(format);
    }

    public Object fromString(String string, ConverterContext context) {
        try {
            return new Double(getNumberFormat().parse(string).doubleValue());
        }
        catch (ParseException e) {
            return null;
        }
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }
}